package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface SearchTracksState {
    object Loading : SearchTracksState
    data class Error(val message: String) : SearchTracksState
    data class Content(val data: List<Track>) : SearchTracksState
}
