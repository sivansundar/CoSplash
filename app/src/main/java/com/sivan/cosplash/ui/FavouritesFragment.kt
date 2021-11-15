package com.sivan.cosplash.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import com.sivan.cosplash.R
import com.sivan.cosplash.data.Photo
import com.sivan.cosplash.databinding.FragmentFavouritesBinding
import com.sivan.cosplash.paging.PagingLoadStateAdapter
import com.sivan.cosplash.util.OnItemClick
import com.sivan.cosplash.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class FavouritesFragment : Fragment(), OnItemClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    lateinit var binding: FragmentFavouritesBinding

    lateinit var adapter: CoSplashFavouriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavouritesBinding.inflate(inflater)
        setupRecyclerView()

        getFavouritesList()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = CoSplashFavouriteAdapter(this)

        val headerAdapter = PagingLoadStateAdapter { adapter.retry() }
        val footerAdapter = PagingLoadStateAdapter { adapter.retry() }

        val gridLayoutManager = GridLayoutManager(context, 2)

        binding.apply {
            favouritesRV.adapter = adapter
            favouritesRV.layoutManager = gridLayoutManager
        }
    }

    private fun getFavouritesList() {
        lifecycleScope.launch {

            mainViewModel.getFavouritesList().collectLatest {
                adapter.submitData(it)
                val res = it.map { item ->
                    item.id
                }
                Toast.makeText(context, "List : $res", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavouritesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavouritesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClick(photo: Photo) {
        val action = FavouritesFragmentDirections.actionFavouritesFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }
}
