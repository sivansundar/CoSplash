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
import com.sivan.cosplash.network.entity.toPhoto
import com.sivan.cosplash.util.OnItemClick

class CoSplashPhotoAdapter(private val listener: OnItemClick) : PagingDataAdapter<UnsplashPhotoEntity, CoSplashPhotoAdapter.PhotoViewHolder>(
    PHOTO_COMPARATOR
) {

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
        RecyclerView.ViewHolder(binding.root) {

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

        fun bind(photo: UnsplashPhotoEntity) {
            binding.apply {
                imageView.load(photo.image_urls.thumb) {
                    crossfade(true)
                    placeholder(R.drawable.ic_baseline_photo_72)
                    error(R.drawable.ic_baseline_error_outline_72)
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
