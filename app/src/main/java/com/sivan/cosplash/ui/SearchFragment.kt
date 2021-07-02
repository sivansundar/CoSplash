package com.sivan.cosplash.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButtonToggleGroup
import com.sivan.cosplash.CoSplashPhotoAdapter
import com.sivan.cosplash.R
import com.sivan.cosplash.databinding.FragmentSearchBinding
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity
import com.sivan.cosplash.paging.PagingLoadStateAdapter
import com.sivan.cosplash.util.OnItemClick
import com.sivan.cosplash.util.RadioGridGroup
import com.sivan.cosplash.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import timber.log.Timber

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SearchFragment : Fragment(), OnItemClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding : FragmentSearchBinding
    lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val args by navArgs<SearchFragmentArgs>()
    private val mainViewModel by viewModels<MainViewModel>()

    lateinit var adapter: CoSplashPhotoAdapter

    var COLOR_STATE : Int = 0

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

        initRecyclerView()

        initFirstCall()

        binding.apply {
            toolbar.title = args.searchTerm

            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            
            toolbar.setOnMenuItemClickListener {
                // Show Filter modal sheet with options
                binding.filterCoordinatorLayout.isVisible = true
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                getPref()

                true
            }
        }


        return binding.root
    }

    private fun initFirstCall() {
        lifecycleScope.launch(Dispatchers.Main) {

            mainViewModel.preferenceFlow.collectLatest {
                Timber.d("Apply : Option : $it")
                val item = it.copy()
                item.query = args.searchTerm
                mainViewModel.updateFilterOptions(item)
            }


        }
    }

    private fun initRecyclerView() {
        adapter = CoSplashPhotoAdapter(this)
        val headerAdapter = PagingLoadStateAdapter { adapter.retry() }
        val footerAdapter = PagingLoadStateAdapter { adapter.retry() }

        val concatAdapter = adapter.withLoadStateHeaderAndFooter(
            header = headerAdapter,
            footer = footerAdapter
        )

        val gridLayoutManager = GridLayoutManager(context, 2)

        binding.apply {
            searchResultRecyclerView.adapter = concatAdapter
            searchResultRecyclerView.layoutManager = gridLayoutManager

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

            retryButton.setOnClickListener {
                adapter.retry()
            }
        }

        adapter.addLoadStateListener { combinedLoadStates ->
            binding.apply {
                progressCircular.isVisible = combinedLoadStates.source.refresh is LoadState.Loading
                searchResultRecyclerView.isVisible = combinedLoadStates.source.refresh is LoadState.NotLoading
                retryButton.isVisible = combinedLoadStates.source.refresh is LoadState.Error
                loadStateCollectionText.isVisible = combinedLoadStates.source.refresh is LoadState.Error
            }

        }


        getSearchResults()


    }

    private fun getSearchResults() {
        mainViewModel.searchOptions.observe(viewLifecycleOwner, {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })
    }

    private fun initFilterSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.filterBottomSheetRootLayout.filterRootLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when(newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.filterCoordinatorLayout.isVisible = false
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        binding.filterBottomSheetRootLayout.apply {
            filterToolbar.setOnMenuItemClickListener {
                Toast.makeText(context, "close", Toast.LENGTH_SHORT).show()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                true
            }

            colorToggleButtonGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                when(checkedId) {

                    R.id.any_color_button -> {
                        COLOR_STATE = COLOR_ANY

                        changeTonesHolderVisibility(false)
                    }

                    R.id.bw_color_button -> {
                        COLOR_STATE = COLOR_BW

                        changeTonesHolderVisibility(false)
                    }

                    R.id.tones_color_button -> {
                        COLOR_STATE = COLOR_TONES
                        changeTonesHolderVisibility(true)
                    }

                }
            }

            clearButton.setOnClickListener {
                setDefaultSelections()
            }

            applyButton.setOnClickListener {

                updateSortBySelection(sortByToggleButtonGroup)

                updateColorSelection(tonesToggleButtonGroup)

                updateOrientationSelection(orientationToggleGroup)

                updateContentFilterSelection(contentFilterGroup)

                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

                lifecycleScope.launch(Dispatchers.Main) {

                  val item = mainViewModel.preferenceFlow.last()
                    mainViewModel.updateFilterOptions(item)

                    Timber.d("Apply : Option : $item")
                }
            }
        }

    }

    private fun updateContentFilterSelection(contentFilterGroup: MaterialButtonToggleGroup) {
        when(contentFilterGroup.checkedButtonId) {
            R.id.content_filter_low_button -> mainViewModel.updateContentFilter(CONTENT_FILTER_LOW_KEY)
            R.id.content_filter_high_button -> mainViewModel.updateContentFilter(CONTENT_FILTER_HIGH_KEY)
        }
    }

    private fun updateOrientationSelection(orientationToggleGroup: MaterialButtonToggleGroup) {
        when(orientationToggleGroup.checkedButtonId) {
            R.id.orientation_any_button -> mainViewModel.updateOrientation(ORIENTATION_ANY_KEY)
            R.id.orientation_portrait_button -> mainViewModel.updateOrientation(ORIENTATION_PORTRAIT_KEY)
            R.id.orientation_landscape_button -> mainViewModel.updateOrientation(ORIENTATION_LANDSCAPE_KEY)
            R.id.orientation_square_button -> mainViewModel.updateOrientation(ORIENTATION_SQUARE_KEY)

        }
    }

    private fun updateSortBySelection(sortByToggleButtonGroup: MaterialButtonToggleGroup) {
        when(sortByToggleButtonGroup.checkedButtonId) {
            R.id.relevance_button -> mainViewModel.updateSortBy(SORT_BY_RELEVANCE_KEY)
            R.id.newest_button -> mainViewModel.updateSortBy(SORT_BY_NEWEST_KEY)

            else -> mainViewModel.updateSortBy("")
        }
    }

    private fun changeTonesHolderVisibility(state : Boolean) {
        binding.filterBottomSheetRootLayout.tonesTitle.isVisible = state
        binding.filterBottomSheetRootLayout.tonesToggleButtonGroup.isVisible = state
    }

    private fun getPref() {
        lifecycleScope.launch(Dispatchers.Main) {
            mainViewModel.preferenceFlow.collect {
//                Toast.makeText(context, "Preds : $it", Toast.LENGTH_SHORT).show()
//                Timber.d("Prefs : $it")
            }
        }
    }

    private fun updateColorSelection(
        tonesToggleButtonGroup: RadioGridGroup,
    ) {

       when(COLOR_STATE) {
           COLOR_ANY -> mainViewModel.updateColor("")

           COLOR_BW -> mainViewModel.updateColor("black_and_white")

           COLOR_TONES -> updateSelectedTone(tonesToggleButtonGroup)

       }



    }

    private fun updateSelectedTone(tonesToggleButtonGroup: RadioGridGroup) {
        when(tonesToggleButtonGroup.checkedCheckableImageButtonId) {
            R.id.yellow_icon -> mainViewModel.updateColor("yellow")
            R.id.orange_icon -> mainViewModel.updateColor("orange")
            R.id.red_icon -> mainViewModel.updateColor("red")
            R.id.purple_icon -> mainViewModel.updateColor("purple")
            R.id.magenta_icon -> mainViewModel.updateColor("magenta")
            R.id.green_icon -> mainViewModel.updateColor("green")
            R.id.teal_icon -> mainViewModel.updateColor("teal")
            R.id.blue_icon -> mainViewModel.updateColor("blue")
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

        private const val COLOR_ANY = 0
        private const val COLOR_BW = 1
        private const val COLOR_TONES = 2

        private const val SORT_BY_RELEVANCE_KEY = "relevance"
        private const val SORT_BY_NEWEST_KEY = "newest"

        private const val ORIENTATION_ANY_KEY = ""
        private const val ORIENTATION_PORTRAIT_KEY = "portrait"
        private const val ORIENTATION_LANDSCAPE_KEY = "landscape"
        private const val ORIENTATION_SQUARE_KEY = "squarish"

        private const val CONTENT_FILTER_LOW_KEY = "low"
        private const val CONTENT_FILTER_HIGH_KEY = "high"

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

    override fun onItemClick(photo: UnsplashPhotoEntity) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }
}