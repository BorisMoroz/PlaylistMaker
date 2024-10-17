package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.models.Track

class SearchViewModel : ViewModel() {
    private val SearchTracksUseCase = Creator.provideSearchTracksUseCase()
    private val SearchHistoryInteractor = Creator.provideSearchHistoryInteractor()

    private val state = MutableLiveData<SearchTracksState>()

    private var lastQuery: String = ""

    fun getState(): LiveData<SearchTracksState> = state

    fun search(query: String) {
        if (query != lastQuery) {
            lastQuery = query

            state.value = SearchTracksState.Loading

            val consumer = object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {

                    when (data) {
                        is ConsumerData.Error -> {
                            val error = SearchTracksState.Error(data.message)
                            state.postValue(error)
                        }

                        is ConsumerData.Data -> {
                            val content = SearchTracksState.Content(data.value)
                            state.postValue(content)
                        }
                    }
                }
            }
            if (query.isNotEmpty()) {
                SearchTracksUseCase.execute(query, consumer)
            }
        }
    }

    fun getSearchHistoryTracks(): List<Track> {
        return SearchHistoryInteractor.getSearchHistoryTracks()
    }

    fun addSearchHistoryTrack(newTrack: Track) {
        SearchHistoryInteractor.addTrack(newTrack)
    }

    fun clearSearchHistory() {
        SearchHistoryInteractor.clear()
    }

    fun isSearchHistoryEmpty(): Boolean {
        return SearchHistoryInteractor.isEmpty()
    }
}