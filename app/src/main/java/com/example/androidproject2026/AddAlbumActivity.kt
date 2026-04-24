package com.example.androidproject2026

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URLEncoder
import java.net.URL
import java.nio.charset.StandardCharsets

class AddAlbumActivity : AppCompatActivity() {

    private lateinit var etQuery: EditText
    private lateinit var btnQuery: Button
    private lateinit var tvResult: TextView
    private lateinit var rvSearchResults: RecyclerView
    private val searchResults = mutableListOf<Album>()
    private lateinit var searchResultAdapter: SearchResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_album)

        etQuery = findViewById(R.id.etQuery)
        btnQuery = findViewById(R.id.btnQuery)
        tvResult = findViewById(R.id.tvResult)
        rvSearchResults = findViewById(R.id.rvSearchResults)
        rvSearchResults.layoutManager = LinearLayoutManager(this)
        searchResultAdapter = SearchResultAdapter(searchResults) { album ->
            val added = AlbumRepository.addAlbum(album)
            if (added) {
                Toast.makeText(this, "已添加：${album.albumName}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "添加失败：该专辑已在收藏中", Toast.LENGTH_SHORT).show()
            }
        }
        rvSearchResults.adapter = searchResultAdapter

        btnQuery.setOnClickListener {
            queryAlbum()
        }
    }

    private fun queryAlbum() {
        val keyword = etQuery.text.toString().trim()
        if (keyword.isEmpty()) {
            tvResult.text = "请输入关键词后再查询"
            searchResults.clear()
            searchResultAdapter.notifyDataSetChanged()
            return
        }

        btnQuery.isEnabled = false
        tvResult.text = "查询中..."
        searchResults.clear()
        searchResultAdapter.notifyDataSetChanged()

        Thread {
            try {
                val encoded = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString())
                val albumApiUrl =
                    "https://itunes.apple.com/search?term=$encoded&media=music&entity=album&limit=25"
                val songApiUrl =
                    "https://itunes.apple.com/search?term=$encoded&media=music&entity=song&limit=25"

                val albumResults = parseAlbums(fetchApi(albumApiUrl))
                val songResults = parseAlbums(fetchApi(songApiUrl))
                val merged = mergeAlbums(albumResults, songResults)
                val rankedAlbums = rankAlbums(merged, keyword)

                runOnUiThread {
                    btnQuery.isEnabled = true
                    if (rankedAlbums.isEmpty()) {
                        tvResult.text = "未找到匹配的专辑信息，请换个关键词"
                    } else {
                        tvResult.text = "共找到 ${rankedAlbums.size} 条匹配结果，请点击右侧按钮添加收藏"
                        searchResults.clear()
                        searchResults.addAll(rankedAlbums)
                        searchResultAdapter.notifyDataSetChanged()
                    }
                }
            } catch (_: Exception) {
                runOnUiThread {
                    btnQuery.isEnabled = true
                    tvResult.text = "查询失败，请检查网络后重试"
                    searchResults.clear()
                    searchResultAdapter.notifyDataSetChanged()
                }
            }
        }.start()
    }

    private fun fetchApi(apiUrl: String): String {
        val connection = (URL(apiUrl).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 10000
            readTimeout = 10000
        }
        return connection.inputStream.bufferedReader().use { it.readText() }
    }

    private fun parseAlbums(rawJson: String): List<Album> {
        val root = JSONObject(rawJson)
        if (root.optInt("resultCount", 0) <= 0) {
            return emptyList()
        }

        val results = root.optJSONArray("results") ?: JSONArray()
        val albums = mutableListOf<Album>()
        for (i in 0 until results.length()) {
            val albumObj = results.optJSONObject(i) ?: continue
            val albumName = albumObj.optString("collectionName", "").trim()
            if (albumName.isBlank()) continue

            val artistName = albumObj.optString("artistName", "未知歌手")
            val genre = albumObj.optString("primaryGenreName", "未知流派")
            val releaseDate = albumObj.optString("releaseDate", "")
            val releaseYear = if (releaseDate.length >= 4) releaseDate.substring(0, 4) else "未知年份"
            val artworkUrl = artworkUrlFromJson(albumObj)

            albums.add(
                Album(
                    albumName = albumName,
                    artistName = artistName,
                    genre = genre,
                    releaseYear = releaseYear,
                    artworkUrl = artworkUrl
                )
            )
        }
        return albums
    }

    private fun artworkUrlFromJson(obj: JSONObject): String {
        val candidates = listOf(
            obj.optString("artworkUrl100", ""),
            obj.optString("artworkUrl600", ""),
            obj.optString("artworkUrl60", "")
        )
        return candidates.firstOrNull { it.isNotBlank() }?.trim().orEmpty()
    }

    private fun mergeAlbums(primary: List<Album>, secondary: List<Album>): List<Album> {
        val mergedMap = linkedMapOf<String, Album>()
        (primary + secondary).forEach { album ->
            val key = "${album.albumName.lowercase()}|${album.artistName.lowercase()}|${album.releaseYear}"
            val existing = mergedMap[key]
            if (existing == null) {
                mergedMap[key] = album
            } else if (existing.artworkUrl.isBlank() && album.artworkUrl.isNotBlank()) {
                mergedMap[key] = album
            }
        }
        return mergedMap.values.toList()
    }

    private fun rankAlbums(albums: List<Album>, keyword: String): List<Album> {
        val normalizedKeyword = keyword.lowercase().trim()
        val tokens = normalizedKeyword.split(Regex("\\s+")).filter { it.isNotBlank() }

        return albums.sortedByDescending { album ->
            val searchable = "${album.albumName} ${album.artistName}".lowercase()
            var score = 0
            if (normalizedKeyword.isNotBlank() && searchable.contains(normalizedKeyword)) {
                score += 10
            }
            tokens.forEach { token ->
                if (searchable.contains(token)) {
                    score += 3
                }
            }
            score
        }
    }
}
