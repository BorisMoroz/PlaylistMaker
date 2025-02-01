package com.practicum.playlistmaker.playlists.domain.repository

import com.practicum.playlistmaker.playlists.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun updatePlayList(playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToPlayList(track: Track, playlist: Playlist)
}



