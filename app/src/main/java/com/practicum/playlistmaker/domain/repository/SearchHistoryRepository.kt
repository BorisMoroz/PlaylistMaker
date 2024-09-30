package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun loadSearchHistory() : List<Track>
    fun saveSearchHistory(tracks : List<Track>)
}