package com.example.androidproject2026

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAddAlbum: Button = findViewById(R.id.btnAddAlbum)
        val btnViewCollection: Button = findViewById(R.id.btnViewCollection)

        btnAddAlbum.setOnClickListener {
            startActivity(Intent(this, AddAlbumActivity::class.java))
        }

        btnViewCollection.setOnClickListener {
            startActivity(Intent(this, AlbumListActivity::class.java))
        }
    }
}
