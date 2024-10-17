package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {
    fun getSearchHistoryTracks() : List<Track>
    fun addTrack(newTrack : Track)
    fun clear()
    fun isEmpty() : Boolean
}