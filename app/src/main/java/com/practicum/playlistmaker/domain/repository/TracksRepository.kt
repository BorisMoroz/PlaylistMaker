package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.Resource
import com.practicum.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(text : String): Resource<List<Track>>
}