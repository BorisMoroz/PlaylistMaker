package com.practicum.playlistmaker.playlists.data.repository

import com.practicum.playlistmaker.favorites.data.converters.TrackDbConverter
import com.practicum.playlistmaker.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.favorites.domain.repository.FavoriteTracksRepository
import com.practicum.playlistmaker.playlists.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.playlists.data.converters.PlaylistsTrackDbConverter
import com.practicum.playlistmaker.playlists.domain.models.Playlist
import com.practicum.playlistmaker.playlists.domain.repository.PlaylistsRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListsRepositoryImpl(private val appDatabase: AppDatabase, private val playlistDbConverter: PlaylistDbConverter, private val playlistsTrackDbConverter: PlaylistsTrackDbConverter) : PlaylistsRepository {
    override suspend fun addPlaylist(playlist: Playlist){
        playlist.id = 0
        appDatabase.playlistDao().insertPlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun updatePlayList(playlist: Playlist){
        appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(playlists.map{playlist -> playlistDbConverter.map(playlist)})
    }

    override suspend fun addTrackToPlayList(track: Track, playlist: Playlist){
        playlist.tracksIds.add(track.trackId)
        playlist.tracksNum++

        val recs =  appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlist))

        appDatabase.playlistsTrackDao().insertPlaylistsTrack(playlistsTrackDbConverter.map(track))
    }
}
