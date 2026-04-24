package com.example.androidproject2026

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

object AlbumRepository {
    private const val PREFS_NAME = "album_collection"
    private const val KEY_ALBUMS_JSON = "albums_json"

    private lateinit var prefs: SharedPreferences
    private val albums = mutableListOf<Album>()
    private var initialized = false

    fun init(context: Context) {
        if (initialized) return
        prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        loadFromDisk()
        initialized = true
    }

    private fun loadFromDisk() {
        albums.clear()
        val raw = prefs.getString(KEY_ALBUMS_JSON, null) ?: return
        try {
            val arr = JSONArray(raw)
            for (i in 0 until arr.length()) {
                val o = arr.optJSONObject(i) ?: continue
                val name = o.optString("albumName", "").trim()
                if (name.isEmpty()) continue
                albums.add(
                    Album(
                        albumName = name,
                        artistName = o.optString("artistName", "未知歌手"),
                        genre = o.optString("genre", "未知流派"),
                        releaseYear = o.optString("releaseYear", "未知年份"),
                        artworkUrl = o.optString("artworkUrl", "")
                    )
                )
            }
        } catch (_: Exception) {
            albums.clear()
        }
    }

    private fun persist() {
        val arr = JSONArray()
        albums.forEach { a ->
            arr.put(
                JSONObject().apply {
                    put("albumName", a.albumName)
                    put("artistName", a.artistName)
                    put("genre", a.genre)
                    put("releaseYear", a.releaseYear)
                    put("artworkUrl", a.artworkUrl)
                }
            )
        }
        prefs.edit().putString(KEY_ALBUMS_JSON, arr.toString()).apply()
    }

    private fun albumKey(album: Album): String {
        return "${album.albumName.trim().lowercase()}|${album.artistName.trim().lowercase()}|${album.releaseYear.trim()}"
    }

    fun addAlbum(album: Album): Boolean {
        if (albums.any { albumKey(it) == albumKey(album) }) {
            return false
        }
        albums.add(album)
        persist()
        return true
    }

    fun removeAlbum(album: Album): Boolean {
        val index = albums.indexOfFirst { albumKey(it) == albumKey(album) }
        if (index < 0) {
            return false
        }
        albums.removeAt(index)
        persist()
        return true
    }

    fun getAlbums(): List<Album> = albums.toList()
}
