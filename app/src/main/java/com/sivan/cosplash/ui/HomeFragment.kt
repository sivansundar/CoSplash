package com.sivan.cosplash.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.sivan.cosplash.CoSplashPhotoAdapter
import com.sivan.cosplash.databinding.FragmentHomeBinding
import com.sivan.cosplash.util.hideKeyboard
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity
import com.sivan.cosplash.paging.PagingLoadStateAdapter
import com.sivan.cosplash.util.OnItemClick
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
class HomeFragment : Fragment(), OnItemClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentHomeBinding

    lateinit var searchBoxTextInput : TextInputEditText

    lateinit var adapter: CoSplashPhotoAdapter

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

        //getCollection()

        return binding.root
    }

    private fun getCollection() {
        mainViewModel.fetchDefaultCollection().observe(viewLifecycleOwner, {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })

//          mainViewModel.imageSearchResult.observe(viewLifecycleOwner, {
//            adapter.submitData(viewLifecycleOwner.lifecycle, it)
//        })

    }

    private fun bindUIComponents() {
        searchBoxTextInput = binding.searchBoxTextInput

        setupSearchBox()

        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        adapter = CoSplashPhotoAdapter(this)

        val headerAdapter = PagingLoadStateAdapter { adapter.retry() }
        val footerAdapter = PagingLoadStateAdapter { adapter.retry() }

        val concatAdapter = adapter.withLoadStateHeaderAndFooter(
            header = headerAdapter,
            footer = footerAdapter
        )

        val gridLayoutManager = GridLayoutManager(context, 2)

        binding.apply {
            recyclerView.adapter = concatAdapter
            recyclerView.layoutManager = gridLayoutManager

            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0 && headerAdapter.itemCount > 0) {
                        2
                        /*
                            If we are at first position and header exists,
                            set span size to 2 so that the entire width is taken
                            */

                    } else if (position == concatAdapter.itemCount - 1 && footerAdapter.itemCount > 0) {
                        2
                        /*
                        At last position and footer exists,
                        set span size to 2 so that the entire width is taken
                        */

                    } else {
                        1
                        // If we are not at the top or bottom positions in the list, then set span size to 1

                    }
                }
            }

        }


        adapter.addLoadStateListener { combinedLoadStates ->
            binding.apply {
                progressCircular.isVisible = combinedLoadStates.source.refresh is LoadState.Loading
                recyclerView.isVisible = combinedLoadStates.source.refresh is LoadState.NotLoading
                retryButton.isVisible = combinedLoadStates.source.refresh is LoadState.Error
                loadStateCollectionText.isVisible = combinedLoadStates.source.refresh is LoadState.Error
            }

        }

        binding.retryButton.setOnClickListener {
            adapter.retry()
        }
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
        //mainViewModel.searchImages(searchText)

        // Navigate to search fragment
        val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment(searchText)
        findNavController().navigate(action)
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

    override fun onItemClick(photoEntity: UnsplashPhotoEntity) {
        // Handles image clicks from the recyclerView. We can use this to navigate to another fragment
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(photoEntity)
        findNavController().navigate(action)

    }
}