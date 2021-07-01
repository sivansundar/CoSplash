package com.sivan.cosplash.ui

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.sivan.cosplash.R
import com.sivan.cosplash.databinding.ActivityMainBinding.bind
import com.sivan.cosplash.databinding.FragmentDetailsBinding
import com.sivan.cosplash.databinding.FragmentSearchBinding
import com.sivan.cosplash.util.RadioGridGroup

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding : FragmentSearchBinding
    lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val args by navArgs<SearchFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater)
        initFilterSheet()
        binding.apply {
            toolbar.title = args.searchTerm

            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            
            toolbar.setOnMenuItemClickListener {
                // Show Filter modal sheet with options
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                true
            }
        }


        return binding.root
    }

    private fun initFilterSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.filterBottomSheetRootLayout.filterRootLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        
        binding.filterBottomSheetRootLayout.apply {
            filterToolbar.setOnMenuItemClickListener {
                Toast.makeText(context, "close", Toast.LENGTH_SHORT).show()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                true
            }

            colorToggleButtonGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                when(checkedId) {
                    R.id.any_color_button -> {
                        tonesTitle.isVisible = true
                        tonesToggleButtonGroup.isVisible = true
                    }

                    R.id.bw_color_button -> {
                        tonesTitle.isVisible = false
                        tonesToggleButtonGroup.isVisible = false
                    }
                }
            }

            clearButton.setOnClickListener {
                setDefaultSelections()
            }

            applyButton.setOnClickListener {
                when(tonesToggleButtonGroup.checkedCheckableImageButtonId) {
                    R.id.yellow_icon -> {
                        Toast.makeText(context, "YELLOW", Toast.LENGTH_SHORT).show()
                    }
                    R.id.orange_icon -> {

                    }
                }
            }


        }

    }

    private fun setDefaultSelections() {
        binding.filterBottomSheetRootLayout.apply {
            sortByToggleButtonGroup.clearChecked()
            anyColorButton.isChecked = true
            tonesToggleButtonGroup.clearCheck()
            orientationAnyButton.isChecked = true
            contentFilterGroup.clearChecked()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}