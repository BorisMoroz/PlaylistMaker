package com.practicum.playlistmaker.playlists.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favorites.domain.interactor.FavoriteTracksInteractor
import com.practicum.playlistmaker.media.ui.FavoriteTracksState
import com.practicum.playlistmaker.playlists.domain.interactor.PlaylistsInteractor
import com.practicum.playlistmaker.playlists.domain.models.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(val playlistsInteractor: PlaylistsInteractor) : ViewModel() {
    fun addPlaylist(playlist: Playlist){
        viewModelScope.launch{
            playlistsInteractor.addPlaylist(playlist)
        }
    }

    fun updatePlaylist(playlist: Playlist){
        viewModelScope.launch{
            playlistsInteractor.updatePlaylist(playlist)
        }
    }
}

class FavoriteTracksViewModel(val favoriteTracksInteractor: FavoriteTracksInteractor) : ViewModel() {
    private val state = MutableLiveData<FavoriteTracksState>()

    fun getState(): LiveData<FavoriteTracksState> = state

    fun getFavoriteTracks(){
        viewModelScope.launch{
            favoriteTracksInteractor
                .getTracks()
                .collect{ result ->
                    if(!result.isEmpty()) {
                        val content = FavoriteTracksState.Content(result)
                        state.postValue(content)
                    }
                    else{
                        state.postValue(FavoriteTracksState.Empty)
                    }
                }
        }
    }
}