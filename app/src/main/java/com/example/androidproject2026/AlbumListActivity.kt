package com.example.androidproject2026

import android.os.Bundle
import android.view.View
import android.widget.TextView
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
        val albums = AlbumRepository.getAlbums()
        rvAlbums.adapter = AlbumAdapter(albums)
        tvEmpty.visibility = if (albums.isEmpty()) View.VISIBLE else View.GONE
    }
}
