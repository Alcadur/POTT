package pl.hamsterdev.pott

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pl.hamsterdev.pott.databinding.FragmentAddItemFormBinding
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

class AddItemFormFragment : Fragment() {

    private lateinit var binding: FragmentAddItemFormBinding
    private lateinit var storeManager: StoreManager

    companion object {
        fun newInstance() = AddItemFormFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAddItemFormBinding.inflate(inflater, container, false)
        storeManager = StoreManager(requireContext())
        binding.expDateField.setOnFocusChangeListener { _,isFocused ->
            if (isFocused) {
                handleDataPicker()
                binding.expDateField.clearFocus()
            }
        }

        binding.saveItemButton.setOnClickListener clickAction@{
            val item  = collectData()
            val errors = item.validate()
            if (errors.isNotEmpty()) {
                Toast.makeText(requireContext(), errors.first(), Toast.LENGTH_SHORT).show()

                return@clickAction
            }

            storeManager.addItem(item)

            val now = Calendar.getInstance(TimeZone.getDefault()).toInstant()

            val notificationDate = item.expireAtAsCalendar
            notificationDate.add(Calendar.DAY_OF_MONTH, Consts.EXPIRE_NOTIFICATION_WORKER_DAYS_BEFORE_EXPIRE)
            notificationDate.set(Calendar.HOUR_OF_DAY, Consts.EXPIRE_NOTIFICATION_WORKER_HOUR_OF_DAY)
            notificationDate.set(Calendar.MINUTE, Consts.EXPIRE_NOTIFICATION_WORKER_MINUTES)
            notificationDate.set(Calendar.SECOND, Consts.EXPIRE_NOTIFICATION_WORKER_SECOND)

            val secondsLeftToNotification = Duration.between(now, notificationDate.toInstant()).toSeconds()

            NotificationUtil(requireContext()).scheduleExpireNotification(item, secondsLeftToNotification)

            findNavController().navigate(R.id.action_addItemForm_to_FirstFragment)
        }


        return binding.root
    }

    private fun handleDataPicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dateFormat = SimpleDateFormat("dd/MM/YYYY")

        val datePicker = DatePickerDialog(
            requireActivity(),
            {_, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
                val date = Date(calendar.timeInMillis)

                binding.expDateField.setText(dateFormat.format(date))
            },
            year,
            month,
            day
        )

        datePicker.show()
    }

    private fun collectData(): ItemModel {
        val name = binding.itemNameField.text.toString()
        val quantityField =  binding.quantityField.text
        val expField = binding.expDateField.text
        var quantity = 0
        var expireAt = -1L

        if (quantityField.isNotBlank()) {
           quantity = quantityField.toString().toInt()
        }

        if (expField.isNotBlank()) {
            val expireDateArray = binding.expDateField.text.split('/').map { value -> value.toInt() }
            val calendar = Calendar.getInstance(TimeZone.getDefault())
            calendar.set(expireDateArray[2], expireDateArray[1].toInt() - 1, expireDateArray[0])

            expireAt = calendar.timeInMillis
        }

        return ItemModel(name = name, quantity = quantity, expireAt = expireAt)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}