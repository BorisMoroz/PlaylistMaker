package com.practicum.playlistmaker.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favorites.domain.interactor.FavoriteTracksInteractor
import com.practicum.playlistmaker.playlists.domain.interactor.PlaylistsInteractor
import kotlinx.coroutines.launch

class PlayListsViewModel(val playlistsInteractor: PlaylistsInteractor) : ViewModel () {
    private val state = MutableLiveData<PlaylistsState>()

    fun getState(): LiveData<PlaylistsState> = state

    fun getPlaylists(){
        viewModelScope.launch{
            playlistsInteractor
                .getPlaylists()
                .collect{ result ->
                    if(!result.isEmpty()) {
                        val content = PlaylistsState.Content(result)
                        state.postValue(content)
                    }
                    else{
                        state.postValue(PlaylistsState.Empty)
                    }
                }
        }
    }
}