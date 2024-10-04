package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.interactor.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) : SearchHistoryInteractor {
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