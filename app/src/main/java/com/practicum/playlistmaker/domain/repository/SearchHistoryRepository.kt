package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun loadTracks()
    fun saveTracks()
    fun getSearchHistoryTracks() : List<Track>
    fun addTrack(newTrack : Track)
    fun clear()
    fun isEmpty() : Boolean
}