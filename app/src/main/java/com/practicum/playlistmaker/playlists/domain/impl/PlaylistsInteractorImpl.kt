package com.practicum.playlistmaker.playlists.domain.impl

import com.practicum.playlistmaker.favorites.domain.interactor.FavoriteTracksInteractor
import com.practicum.playlistmaker.favorites.domain.repository.FavoriteTracksRepository
import com.practicum.playlistmaker.playlists.domain.interactor.PlaylistsInteractor
import com.practicum.playlistmaker.playlists.domain.models.Playlist
import com.practicum.playlistmaker.playlists.domain.repository.PlaylistsRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val playlistsRepository: PlaylistsRepository) :
    PlaylistsInteractor {

    override suspend fun addPlaylist(playlist: Playlist){
        playlistsRepository.addPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist){
        playlistsRepository.updatePlayList(playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>>{
        return playlistsRepository.getPlaylists()
    }

    override suspend fun addTrackToPlayList(track: Track, playlist: Playlist){
        playlistsRepository.addTrackToPlayList(track, playlist)
    }
}
