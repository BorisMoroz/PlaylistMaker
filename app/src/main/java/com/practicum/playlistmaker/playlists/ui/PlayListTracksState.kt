package com.practicum.playlistmaker.playlists.ui

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface PlaylistTracksState {
    data class Content(val data: List<Track>) : PlaylistTracksState
    object Empty : PlaylistTracksState
}