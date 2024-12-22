package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.search.domain.models.Track

interface SearchHistoryRepository {
    suspend fun loadTracks()
    fun saveTracks()
    fun getSearchHistoryTracks() : List<Track>
    fun addTrack(newTrack : Track)
    fun clear()
    fun isEmpty() : Boolean
}