package com.sivan.cosplash.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.sivan.cosplash.CoSplashPhotoAdapter
import com.sivan.cosplash.R
import com.sivan.cosplash.databinding.FragmentHomeBinding
import com.sivan.cosplash.hideKeyboard
import com.sivan.cosplash.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentHomeBinding

    lateinit var searchBoxTextInput : TextInputEditText

    private val mainViewModel by viewModels<MainViewModel>()

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
        binding = FragmentHomeBinding.inflate(inflater)

        bindUIComponents()

        val adapter = CoSplashPhotoAdapter()

        mainViewModel.imageSearchResult.observe(viewLifecycleOwner, {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })


        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
           // recyclerView.isNestedScrollingEnabled = false
            recyclerView.layoutManager = linearLayoutManager

        }

        return binding.root
    }

    private fun bindUIComponents() {
        searchBoxTextInput = binding.searchBoxTextInput

        setupSearchBox()

    }

    private fun setupSearchBox() {
        searchBoxTextInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
                Timber.d("Clicked")
                // Handle Search function. Change switchmap value

                searchPhotos(v.text.toString())

                hideKeyboard()
            }
            true
        }
    }

    private fun searchPhotos(searchText: String) {
        mainViewModel.searchImages(searchText)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}