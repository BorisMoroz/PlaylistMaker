package com.practicum.playlistmaker.media.ui

import com.practicum.playlistmaker.playlists.domain.models.Playlist

sealed interface PlaylistsState {
    data class Content(val data: List<Playlist>) : PlaylistsState
    object Empty : PlaylistsState
}