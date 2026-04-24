package com.example.androidproject2026

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AlbumListActivity : AppCompatActivity() {

    private lateinit var rvAlbums: RecyclerView
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_list)

        rvAlbums = findViewById(R.id.rvAlbums)
        tvEmpty = findViewById(R.id.tvEmpty)
        rvAlbums.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        val albums = AlbumRepository.getAlbums()
        rvAlbums.adapter = AlbumAdapter(albums) { album ->
            val removed = AlbumRepository.removeAlbum(album)
            if (removed) {
                Toast.makeText(this, "已删除：${album.albumName}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "删除失败：未找到该专辑", Toast.LENGTH_SHORT).show()
            }
            refreshList()
        }
        tvEmpty.visibility = if (albums.isEmpty()) View.VISIBLE else View.GONE
    }
}
