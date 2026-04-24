package com.example.androidproject2026

import android.app.Application

class AlbumCollectorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AlbumRepository.init(this)
    }
}
