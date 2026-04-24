package com.example.androidproject2026

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class AlbumAdapter(
    private val albums: List<Album>,
    private val onDeleteClick: (Album) -> Unit
) :
    RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(view, onDeleteClick)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    class AlbumViewHolder(
        itemView: View,
        private val onDeleteClick: (Album) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val ivArtwork: ImageView = itemView.findViewById(R.id.ivArtwork)
        private val tvAlbumName: TextView = itemView.findViewById(R.id.tvAlbumName)
        private val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
        private val tvGenre: TextView = itemView.findViewById(R.id.tvGenre)
        private val tvYear: TextView = itemView.findViewById(R.id.tvYear)
        private val btnDeleteAlbum: Button = itemView.findViewById(R.id.btnDeleteAlbum)

        fun bind(album: Album) {
            tvAlbumName.text = "专辑：${album.albumName}"
            tvArtistName.text = "歌手：${album.artistName}"
            tvGenre.text = "流派：${album.genre}"
            tvYear.text = "发行年份：${album.releaseYear}"

            if (album.artworkUrl.isNotBlank()) {
                ivArtwork.load(album.artworkUrl) {
                    crossfade(true)
                    placeholder(R.drawable.album_placeholder)
                    error(R.drawable.album_placeholder)
                }
            } else {
                ivArtwork.setImageResource(R.drawable.album_placeholder)
            }

            btnDeleteAlbum.setOnClickListener {
                onDeleteClick(album)
            }
        }
    }
}
