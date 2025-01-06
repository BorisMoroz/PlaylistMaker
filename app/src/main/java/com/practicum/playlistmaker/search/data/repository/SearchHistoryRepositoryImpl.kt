package com.practicum.playlistmaker.search.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchHistoryRepositoryImpl(private val sharedPrefs : SharedPreferences, private val gson : Gson, private val appDatabase: AppDatabase) :
    SearchHistoryRepository {
    var tracks: MutableList<Track> = mutableListOf()

    init {
        loadTracks()
    }

    override fun loadTracks() {
        val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, null)

        if (json != null) {
            val typeToken = object : TypeToken<List<TrackDto>>() {}.type
            val resultsDto: List<TrackDto> = gson.fromJson(json, typeToken)
            val results: List<Track> = resultsDto.map {
                Track(
                    it.trackId, it.collectionName, it.releaseDate, it.primaryGenreName, it.country,
                    it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.previewUrl
                )
            }
            tracks = results.toMutableList()
        } else
            tracks = mutableListOf()
    }

    override fun saveTracks() {
        val tracksDto = tracks.map {
            TrackDto(
                it.trackId, it.collectionName, it.releaseDate, it.primaryGenreName, it.country,
                it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.previewUrl
            )
        }

        val json = gson.toJson(tracksDto)

        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    override suspend fun getSearchHistoryTracks(): List<Track> {
        val favoriteTracksIds = appDatabase.favoriteTrackDao().getTracksIds()

        for(track in tracks) {
            track.isFavorite = false
            if (track.trackId in favoriteTracksIds)
                track.isFavorite = true
        }
        return tracks
    }

    override fun addTrack(newTrack: Track) {
        var trackFound = false
        var trackIndex = 0

        for (i in tracks.indices) {
            if (tracks[i].trackId == newTrack.trackId) {
                trackFound = true
                trackIndex = i
                break
            }
        }
        if (trackFound)
            tracks.removeAt(trackIndex)

        tracks.add(0, newTrack)

        if (tracks.size > SEARCH_HISTORY_LENGTH)
            tracks.removeAt(tracks.size - 1)

        saveTracks()
    }

    override fun clear() {
        tracks.clear()
        saveTracks()
    }

    override fun isEmpty(): Boolean {
        return tracks.isEmpty()
    }

    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history"
        private const val SEARCH_HISTORY_LENGTH = 10
    }
}
