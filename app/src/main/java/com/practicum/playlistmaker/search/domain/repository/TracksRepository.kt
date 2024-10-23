package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.models.Track

interface TracksRepository {
    fun searchTracks(text : String): Resource<List<Track>>
}