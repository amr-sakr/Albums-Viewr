package com.example.albumsviewer.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.albumsviewer.databinding.ItemNetworkStateBinding


class PagingLoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PagingLoadingStateAdapter.LoadStateViewHolder>() {


    class LoadStateViewHolder(
        private val binding: ItemNetworkStateBinding,
        retry: () -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.retryBtn.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                retryBtn.isVisible = loadState is LoadState.Error
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
                retryCallback: () -> Unit
            ): LoadStateViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemNetworkStateBinding.inflate(inflater, parent, false)
                return LoadStateViewHolder(binding, retryCallback)
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder {
        return LoadStateViewHolder.from(
            parent
        ) { retry.invoke() }
    }

}