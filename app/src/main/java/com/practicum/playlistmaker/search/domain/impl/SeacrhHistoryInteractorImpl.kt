package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.interactor.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun getSearchHistoryTracks() : List<Track>{
        return repository.getSearchHistoryTracks()
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