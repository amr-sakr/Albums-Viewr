package com.example.albumsviewer.features.albumDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.albumsviewer.data.models.view.Photo
import com.example.albumsviewer.databinding.PhotosListItemBinding

class PhotosAdapter(
    private val listener: (url: String) -> Unit
) : PagingDataAdapter<Photo, PhotosAdapter.PhotosViewHolder>(PhotoDiffUtil) {

    companion object {
        object PhotoDiffUtil : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem == newItem
            }
        }
    }

    class PhotosViewHolder(
        private val binding: PhotosListItemBinding
    ) : ViewHolder(binding.root) {
        fun bind(item: Photo?, listener: (url: String) -> Unit) {
            binding.ivPhoto.load(item?.thumbnailUrl)
            itemView.setOnClickListener {
                listener(item?.url ?: "")
            }
        }

        companion object {
            fun from(parent: ViewGroup): PhotosViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = PhotosListItemBinding.inflate(inflater, parent, false)
                return PhotosViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PhotosViewHolder.from(parent)

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener)
    }
}



