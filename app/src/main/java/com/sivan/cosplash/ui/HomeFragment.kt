package com.sivan.cosplash.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import coil.compose.rememberImagePainter
import com.google.android.material.textfield.TextInputEditText
import com.sivan.cosplash.R
import com.sivan.cosplash.data.Photo
import com.sivan.cosplash.databinding.FragmentHomeBinding
import com.sivan.cosplash.util.OnItemClick
import com.sivan.cosplash.util.hideKeyboard
import com.sivan.cosplash.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

    lateinit var searchBoxTextInput: TextInputEditText

    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_graph)

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

        lifecycleScope.launch {
            mainViewModel.clearFilterOptions()
        }

        //getCollection()

        val listOfImages = listOf<String>(
            "https://images.unsplash.com/photo-1634917694906-ce32fa906bfb?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=464&q=80",
            "https://images.unsplash.com/photo-1634917694906-ce32fa906bfb?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=464&q=80",
            "https://images.unsplash.com/photo-1634917694906-ce32fa906bfb?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=464&q=80",
            "https://images.unsplash.com/photo-1634917694906-ce32fa906bfb?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=464&q=80",
            "https://images.unsplash.com/photo-1634917694906-ce32fa906bfb?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=464&q=80",
            "https://images.unsplash.com/photo-1634917694906-ce32fa906bfb?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=464&q=80",
            "https://images.unsplash.com/photo-1634917694906-ce32fa906bfb?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=464&q=80"
        )

        binding.titleText.setContent {
            TitleText(text = "CoSplash - Compose",
                      modifier = Modifier.fillMaxWidth())
        }

        binding.resultContent.setContent {
            ResultContent(list = listOfImages)
        }

        return binding.root
    }

    @Composable
    fun TitleText(text : String, modifier: Modifier) {
        Text(text = text)
    }
    
    @Composable
    @Preview
    fun TitleTextPreview() {
        TitleText(text = "CoSplash - Compose",
                  modifier = Modifier.fillMaxWidth())
    }

    @Composable
    @Preview
    fun ResultContentPreview(){
        ResultContent(list = listOf(
            "https://images.unsplash.com/photo-1634917694906-ce32fa906bfb?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=464&q=80",
            "https://images.unsplash.com/photo-1634917694906-ce32fa906bfb?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=464&q=80",
            "https://images.unsplash.com/photo-1634917694906-ce32fa906bfb?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=464&q=80",

        ))
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ResultContent(list : List<String>){
        val pagedResult = mainViewModel.fetchDefaultCollection().collectAsLazyPagingItems()
        pagedResult.apply {
            when{
                loadState.refresh is LoadState.Loading -> {
                    //You can add modifier to manage load state when first time response page is loading
                    Timber.d("State : Loading")
                }
                loadState.append is LoadState.NotLoading -> {
                    //You can add modifier to manage load state when next response page is loading
                    Timber.d("State : Not Loading")

                }
                loadState.append is LoadState.Error -> {
                    //You can use modifier to show error message
                    Timber.d("State : Error")

                }
            }
        }
        LazyColumn(){
            items(pagedResult){item ->
                item?.image_urls?.regular?.let { PhotoItem(url = it) }
            }
        }

    }

    @Composable
    fun PhotoItem(url : String) {
        val rememberPainter = rememberImagePainter(url)
        Image(
            painter = rememberPainter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
    
    private fun getCollection() {
        /**
         * Initiates a request to get the default star wars collection. The data recieved is then sent to the CoSplashPhotoAdapter
         * */
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.fetchDefaultCollection().collect {
            }

        }
    }

    private fun bindUIComponents() {
        searchBoxTextInput = binding.searchBoxTextInput

        setupSearchBox()

        //setupRecyclerView()

    }

//    private fun setupRecyclerView() {
//        adapter = CoSplashPhotoAdapter(this)
//
//        val headerAdapter = PagingLoadStateAdapter { adapter.retry() }
//        val footerAdapter = PagingLoadStateAdapter { adapter.retry() }
//
//        val concatAdapter = adapter.withLoadStateHeaderAndFooter(
//            header = headerAdapter,
//            footer = footerAdapter
//        )
//
//        val gridLayoutManager = GridLayoutManager(context, 2)
//
//        binding.apply {
//            recyclerView.adapter = concatAdapter
//            recyclerView.layoutManager = gridLayoutManager
//
//            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//                override fun getSpanSize(position: Int): Int {
//                    return if (position == 0 && headerAdapter.itemCount > 0) {
//                        2
//                        /**
//                        If we are at first position and header exists,
//                        set span size to 2 so that the entire width is taken
//                         **/
//
//                    } else if (position == concatAdapter.itemCount - 1 && footerAdapter.itemCount > 0) {
//                        2
//
//                        /**
//                        If we are last position and footer exists,
//                        set span size to 2 so that the entire width is taken
//                         **/
//
//                    } else {
//                        1
//
//                    }
//                }
//            }
//
//        }
//
//
//        adapter.addLoadStateListener { combinedLoadStates ->
//            binding.apply {
//                progressCircular.isVisible = combinedLoadStates.source.refresh is LoadState.Loading
//                recyclerView.isVisible = combinedLoadStates.source.refresh is LoadState.NotLoading
//                retryButton.isVisible = combinedLoadStates.source.refresh is LoadState.Error
//                loadStateCollectionText.isVisible =
//                    combinedLoadStates.source.refresh is LoadState.Error
//            }
//
//        }
//
//        binding.retryButton.setOnClickListener {
//            adapter.retry()
//        }
//    }

    private fun setupSearchBox() {
        searchBoxTextInput.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchPhotos(v.text.toString())
                hideKeyboard()
            }
            true
        }
    }

    private fun searchPhotos(searchText: String) {
        updateFilterOptions(searchText)

        navigateToSearchFragment()
    }

    private fun navigateToSearchFragment() {
        // Navigate to the search fragment
        val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
        findNavController().navigate(action)
    }

    private fun updateFilterOptions(searchText: String) {
        mainViewModel.updateQuery(searchText)
        mainViewModel.updateFilter()

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

    override fun onItemClick(photo: Photo) {
        // Handles image clicks from the recyclerView. We can use this to navigate to another fragment
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(photo)
        findNavController().navigate(action)

    }
}