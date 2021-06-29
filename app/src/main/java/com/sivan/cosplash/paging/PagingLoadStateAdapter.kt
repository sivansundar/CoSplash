package com.sivan.cosplash.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sivan.cosplash.databinding.ListItemFooterBinding

class PagingLoadStateAdapter(private val retry : () -> Unit) : LoadStateAdapter<PagingLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ListItemFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }



    inner class LoadStateViewHolder(private val binding : ListItemFooterBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {

            when(loadState) {
                is LoadState.Loading -> {
                    binding.apply {
                        progressCircular.visibility = View.VISIBLE
                        retryButton.visibility = View.GONE
                    }
                }

                is LoadState.NotLoading -> {
                    binding.apply {
                        progressCircular.visibility = View.GONE
                        retryButton.visibility = View.GONE
                    }
                }

                is LoadState.Error -> {
                    binding.apply {
                        progressCircular.visibility = View.GONE
                        retryButton.visibility = View.VISIBLE
                    }
                }
            }

//            binding.apply {
//                progressCircular.isVisible = loadState is LoadState.Loading
//                retryButton.isVisible = loadState is LoadState.NotLoading || loadState is LoadState.Error
//            }
        }
    }
}