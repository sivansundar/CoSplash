package com.sivan.cosplash.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.sivan.cosplash.R
import com.sivan.cosplash.data.Photo
import com.sivan.cosplash.databinding.FragmentDetailsBinding
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity
import com.sivan.cosplash.network.entity.toEntity
import com.sivan.cosplash.room.entity.FavouriteCacheEntity
import com.sivan.cosplash.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import timber.log.Timber


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentDetailsBinding

    private val args by navArgs<DetailsFragmentArgs>()

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
        binding = FragmentDetailsBinding.inflate(layoutInflater)

        val photos = args.photos

        // Get initial selection state.
        getFavItemFromVM(photos)

        val images = Json.decodeFromString(UnsplashPhotoEntity.ImageUrls.serializer(), photos.image_urls)
        binding.apply {

            photoView.load(images.full) {
                listener(
                    onSuccess = { request, metadata ->
                        changeProgressIndicatorVisibility(false)
                    },
                    onError = { request, throwable ->
                        changeProgressIndicatorVisibility(false)
                    },
                    onCancel = { request ->
                        changeProgressIndicatorVisibility(false)
                    },
                    onStart = { request ->
                        changeProgressIndicatorVisibility(true)
                    }

                )
                error(R.drawable.ic_baseline_error_outline_72)
            }
        }
        return binding.root
    }

    private fun addToFav(photos: FavouriteCacheEntity) {
        lifecycleScope.launch(Dispatchers.Main) {
            mainViewModel.addFavouriteItem(item = photos)
        }
    }

    private fun getFavItemFromVM(photos: Photo) {
        lifecycleScope.launch {
            mainViewModel.getFavItem(photos.id)

            mainViewModel.selectionState.observe(viewLifecycleOwner, {
                Timber.d("Checked state : ${it}")
                binding.checkBox.isChecked = it
            })
        }

        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                /** Triggers only when a button is pressed. **/
                if (isChecked) {
                    addToFav(photos.toEntity())
                    Toast.makeText(context, "This photo was successfully added to your favourites list", Toast.LENGTH_SHORT).show()
                } else {
                    removeFromFav(photos.id)
                    Toast.makeText(context, "This photo was removed from your favourites list", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun removeFromFav(id: String) {
        lifecycleScope.launch {
            mainViewModel.removeFavouriteItem(id)
        }
    }

    private fun changeProgressIndicatorVisibility(state: Boolean) {
        binding.progressCircular.isVisible = state
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}