package com.practicum.playlistmaker.media.ui

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface FavoriteTracksState {
    data class Content(val data: List<Track>) : FavoriteTracksState
    object Empty : FavoriteTracksState
}