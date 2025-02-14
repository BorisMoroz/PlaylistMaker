package com.practicum.playlistmaker.playlists.domain.interactor

import android.net.Uri
import com.practicum.playlistmaker.playlists.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToPlayList(track: Track, playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist?)
    suspend fun removeTrackFromPlayList(track: Track, playlist: Playlist?)
    suspend fun getPlaylistTracks(playlist: Playlist?): Flow<List<Track>>
    suspend fun saveImageToPrivateStorage(uri: Uri) : String
}