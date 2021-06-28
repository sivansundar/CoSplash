package com.sivan.cosplash

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.load
import com.sivan.cosplash.databinding.PhotoItemBinding
import com.sivan.cosplash.network.entity.UnsplashPhotoEntity

class CoSplashPhotoAdapter : PagingDataAdapter<UnsplashPhotoEntity, CoSplashPhotoAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PhotoViewHolder(binding)
    }


    inner class PhotoViewHolder(private val binding: PhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(photo : UnsplashPhotoEntity) {
            binding.apply {
                imageView.load(photo.image_urls.thumb) {
                    crossfade(true)
                }
            }
        }
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnsplashPhotoEntity>() {
            override fun areItemsTheSame(oldItem: UnsplashPhotoEntity, newItem: UnsplashPhotoEntity) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: UnsplashPhotoEntity, newItem: UnsplashPhotoEntity) =
                oldItem == newItem
        }
    }
}