package com.practicum.playlistmaker.playlists.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlists.domain.interactor.PlaylistsInteractor
import com.practicum.playlistmaker.playlists.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.sharing.domain.interactor.SharingInteractor
import kotlinx.coroutines.launch

class ViewPlaylistViewModel(
    val playlistsInteractor: PlaylistsInteractor,
    val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val state = MutableLiveData<PlaylistTracksState>()

    fun getState(): LiveData<PlaylistTracksState> = state

    fun getPlaylistTacks(playlist: Playlist?) {
        viewModelScope.launch {
            playlistsInteractor
                .getPlaylistTracks(playlist)
                .collect { result ->
                    if (result.isNotEmpty()) {
                        val content = PlaylistTracksState.Content(result)
                        state.postValue(content)
                    } else {
                        state.postValue(PlaylistTracksState.Empty)
                    }
                }
        }
    }

    fun removeTrackFromPlayList(track: Track, playlist: Playlist?) {
        viewModelScope.launch {
            playlistsInteractor.removeTrackFromPlayList(track, playlist)
        }
        getPlaylistTacks(playlist)
    }

    fun deletePlaylist(playlist: Playlist?) {
        viewModelScope.launch {
            playlistsInteractor.deletePlaylist(playlist)
        }
    }

    fun sharePlaylist(message: String) {
        sharingInteractor.sharePlaylist(message)
    }
}