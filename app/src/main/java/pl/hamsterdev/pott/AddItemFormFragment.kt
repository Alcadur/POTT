package pl.hamsterdev.pott

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import pl.hamsterdev.pott.databinding.FragmentAddItemFormBinding
import java.text.SimpleDateFormat
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

        binding.saveItemButton.setOnClickListener {
            val data = collectData()
            storeManager.addItem(data)
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
        val quantity = binding.quantityField.text.toString().toInt()
        val expireDateArray = binding.expDateField.text.split('/').map { value -> value.toInt() }
        val calendar = Calendar.getInstance()
        calendar.set(expireDateArray[2], expireDateArray[1].toInt() - 1, expireDateArray[0], 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return ItemModel(name = name, quantity = quantity, expireAt = calendar.timeInMillis)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}