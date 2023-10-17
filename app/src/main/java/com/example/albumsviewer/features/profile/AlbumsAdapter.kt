package com.example.albumsviewer.features.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.albumsviewer.data.models.view.Album
import com.example.albumsviewer.databinding.AlbumsListItemBinding

class AlbumsAdapter(
    private val listener: (id: Int) -> Unit
) : ListAdapter<Album, AlbumsAdapter.AlbumsViewHolder>(AlbumDiffUtil) {

    companion object {
        object AlbumDiffUtil : DiffUtil.ItemCallback<Album>() {
            override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem == newItem
            }
        }
    }

    class AlbumsViewHolder(
        private val binding: AlbumsListItemBinding
    ) : ViewHolder(binding.root) {
        fun bind(item: Album, listener: (id: Int) -> Unit) {
            binding.tvTitle.text = item.title
            itemView.setOnClickListener {
                listener(item.id)
            }
        }

        companion object {
            fun from(parent: ViewGroup): AlbumsViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = AlbumsListItemBinding.inflate(inflater, parent, false)
                return AlbumsViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AlbumsViewHolder.from(parent)

    override fun onBindViewHolder(holder: AlbumsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener)
    }
}



