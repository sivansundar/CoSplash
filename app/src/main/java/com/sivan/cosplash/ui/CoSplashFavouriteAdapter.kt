package com.sivan.cosplash.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sivan.cosplash.R
import com.sivan.cosplash.databinding.PhotoItemBinding
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity
import com.sivan.cosplash.room.entity.FavouriteCacheEntity
import com.sivan.cosplash.room.entity.toPhoto
import com.sivan.cosplash.ui.CoSplashFavouriteAdapter.*
import com.sivan.cosplash.util.OnItemClick
import kotlinx.serialization.json.Json

class CoSplashFavouriteAdapter(private val listener : OnItemClick) : PagingDataAdapter<FavouriteCacheEntity, FavViewHolder>(
     PHOTO_COMPARATOR
) {


    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FavViewHolder(binding)
    }

    inner class FavViewHolder(private val binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item.toPhoto())
                    }
                }
            }
        }

        fun bind(favouriteCacheEntity: FavouriteCacheEntity) {
            binding.apply {
                val images = Json.decodeFromString(UnsplashPhotoEntity.ImageUrls.serializer(), favouriteCacheEntity.image_urls)
                imageView.load(images.thumb) {
                    crossfade(true)
                    placeholder(R.drawable.ic_baseline_photo_72)
                    error(R.drawable.ic_baseline_error_outline_72)
                }
            }
        }
    }


    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<FavouriteCacheEntity>() {
            override fun areItemsTheSame(oldItem: FavouriteCacheEntity, newItem: FavouriteCacheEntity) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: FavouriteCacheEntity, newItem: FavouriteCacheEntity) =
                oldItem == newItem
        }
    }

}