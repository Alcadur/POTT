package pl.hamsterdev.pott

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pl.hamsterdev.pott.databinding.FragmentListBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private lateinit var storeManager: StoreManager

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        storeManager = StoreManager(requireContext())

        val items = storeManager.getItems()
        items.sortBy { it.daysLeft }

        binding.itemsList.layoutManager = LinearLayoutManager(requireContext())
        binding.itemsList.adapter = CustomAdapter(items)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addItemButton.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_addItemForm)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}