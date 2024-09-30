package com.practicum.playlistmaker.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryRepositoryImpl(private val sharedPrefs : SharedPreferences) : SearchHistoryRepository {
    val myGson = Gson()

    override fun loadSearchHistory() : List<Track>{
        val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, null)

        if (json != null) {
            val typeToken = object : TypeToken<List<TrackDto>>() {}.type
            val resultsDto : List<TrackDto> = myGson.fromJson(json, typeToken)
            val results : List<Track> = resultsDto.map { Track(it.trackId, it.collectionName, it.releaseDate, it.primaryGenreName, it.country,
                it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.previewUrl) }

            return results
        }
        else
            return emptyList()
    }
    override fun saveSearchHistory(tracks : List<Track>){
        val tracksDto = tracks.map { TrackDto(it.trackId, it.collectionName, it.releaseDate, it.primaryGenreName, it.country,
            it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.previewUrl) }

        val json = myGson.toJson(tracksDto)

        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }
    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history"
    }
}
