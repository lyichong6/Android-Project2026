package com.example.androidproject2026

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlbumAdapter(private val albums: List<Album>) :
    RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAlbumName: TextView = itemView.findViewById(R.id.tvAlbumName)
        private val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
        private val tvGenre: TextView = itemView.findViewById(R.id.tvGenre)
        private val tvYear: TextView = itemView.findViewById(R.id.tvYear)

        fun bind(album: Album) {
            tvAlbumName.text = "专辑：${album.albumName}"
            tvArtistName.text = "歌手：${album.artistName}"
            tvGenre.text = "流派：${album.genre}"
            tvYear.text = "发行年份：${album.releaseYear}"
        }
    }
}
