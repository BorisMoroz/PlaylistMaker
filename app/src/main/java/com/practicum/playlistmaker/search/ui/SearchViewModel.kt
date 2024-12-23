package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.interactor.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.use_case.SearchTracksUseCase
import kotlinx.coroutines.launch

class SearchViewModel(val searchTracksUseCase : SearchTracksUseCase, val searchHistoryInteractor : SearchHistoryInteractor) : ViewModel() {
    private val state = MutableLiveData<SearchTracksState>()

    private var lastQuery: String = ""

    fun getState(): LiveData<SearchTracksState> = state

    fun search(query: String, anyway: Boolean) {
        if (query != lastQuery || anyway) {
            lastQuery = query

            state.value = SearchTracksState.Loading

            viewModelScope.launch {
                searchTracksUseCase
                    .execute(query)
                    .collect { result ->
                        when (result) {
                            is Resource.Error -> {
                                val error = SearchTracksState.Error(result.message)
                                state.postValue(error)
                            }
                            is Resource.Success -> {
                                val content = SearchTracksState.Content(result.data)
                                state.postValue(content)
                            }
                        }
                    }
            }
        }
    }

    fun getSearchHistoryTracks(): List<Track> {
        return searchHistoryInteractor.getSearchHistoryTracks()
    }

    fun addSearchHistoryTrack(newTrack: Track) {
        searchHistoryInteractor.addTrack(newTrack)
    }

    fun clearSearchHistory() {
        searchHistoryInteractor.clear()
    }

    fun isSearchHistoryEmpty(): Boolean {
        return searchHistoryInteractor.isEmpty()
    }
}