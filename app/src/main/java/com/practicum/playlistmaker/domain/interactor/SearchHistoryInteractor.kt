package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun getSearchHistoryTracks() : List<Track>
    fun addTrack(newTrack : Track)
    fun clear()
    fun isEmpty() : Boolean
}