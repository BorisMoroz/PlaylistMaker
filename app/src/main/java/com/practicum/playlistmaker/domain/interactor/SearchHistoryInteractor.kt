package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun getHistoryTracks() : List<Track>
    fun loadTracks()
    fun saveTracks()
    fun addTrack(newTrack : Track)
    fun clear()
    fun isEmpty() : Boolean
}