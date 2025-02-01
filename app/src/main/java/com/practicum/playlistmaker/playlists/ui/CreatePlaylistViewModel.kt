package com.practicum.playlistmaker.playlists.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favorites.domain.interactor.FavoriteTracksInteractor
import com.practicum.playlistmaker.media.ui.FavoriteTracksState
import com.practicum.playlistmaker.playlists.domain.interactor.PlaylistsInteractor
import com.practicum.playlistmaker.playlists.domain.models.Playlist
import kotlinx.coroutines.launch

open class CreatePlaylistViewModel(val playlistsInteractor: PlaylistsInteractor) : ViewModel() {
    private val playlistImageAbsolutePath = MutableLiveData<String>()
    fun getplaylistImageAbsolutePath(): LiveData<String> = playlistImageAbsolutePath

    fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor.addPlaylist(playlist)
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor.updatePlaylist(playlist)
        }
    }
    fun saveImageToPrivateStorage(uri: Uri?) {
        var fullFileName = ""

        viewModelScope.launch {
            if (uri != null) {
                fullFileName = playlistsInteractor.saveImageToPrivateStorage(uri)
                playlistImageAbsolutePath.postValue(fullFileName)
            } else {
                playlistImageAbsolutePath.postValue("")
            }
        }
    }
}
