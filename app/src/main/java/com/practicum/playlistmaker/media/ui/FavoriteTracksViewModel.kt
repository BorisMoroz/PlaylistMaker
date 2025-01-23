package com.practicum.playlistmaker.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favorites.domain.interactor.FavoriteTracksInteractor
import kotlinx.coroutines.launch

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