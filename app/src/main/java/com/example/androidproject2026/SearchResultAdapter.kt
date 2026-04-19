package com.example.androidproject2026

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchResultAdapter(
    private val items: List<Album>,
    private val onAddClick: (Album) -> Unit
) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return SearchResultViewHolder(view, onAddClick)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class SearchResultViewHolder(
        itemView: View,
        private val onAddClick: (Album) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvAlbumInfo: TextView = itemView.findViewById(R.id.tvAlbumInfo)
        private val btnAddSingle: Button = itemView.findViewById(R.id.btnAddSingle)

        fun bind(album: Album) {
            tvAlbumInfo.text = """
                专辑：${album.albumName}
                歌手：${album.artistName}
                流派：${album.genre}
                发行年份：${album.releaseYear}
            """.trimIndent()

            btnAddSingle.setOnClickListener {
                onAddClick(album)
            }
        }
    }
}
