package com.example.androidproject2026

object AlbumRepository {
    private val albums = mutableListOf<Album>()

    fun addAlbum(album: Album) {
        albums.add(album)
    }

    fun getAlbums(): List<Album> = albums.toList()
}
