package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.interactor.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override suspend fun getSearchHistoryTracks() : List<Track>{
        var tracks: List<Track> = emptyList()

        tracks = repository.getSearchHistoryTracks()
        return tracks
    }
    override fun addTrack(newTrack : Track){
        repository.addTrack(newTrack)
    }
    override fun clear(){
        repository.clear()
    }
    override fun isEmpty() : Boolean{
        return repository.isEmpty()
    }
}