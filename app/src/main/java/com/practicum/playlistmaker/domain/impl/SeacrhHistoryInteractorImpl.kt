package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.interactor.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    var tracks : MutableList<Track> = mutableListOf()

    init {
        loadTracks()
    }

    override fun getHistoryTracks() : List<Track>{
        return tracks
    }
    override fun loadTracks(){
        tracks = repository.loadSearchHistory().toMutableList()
    }
    override fun saveTracks(){
        repository.saveSearchHistory(tracks.toList())
    }
    override fun addTrack(newTrack : Track){
        var trackFound = false
        var trackIndex = 0

        for (i in tracks.indices) {
            if (tracks[i].trackId == newTrack.trackId) {
                trackFound = true
                trackIndex = i
                break
            }
        }
        if (trackFound)
            tracks.removeAt(trackIndex)

        tracks.add(0, newTrack)

        if (tracks.size > SEARCH_HISTORY_LENGTH)
            tracks.removeAt(tracks.size-1)

        saveTracks()
    }
    override fun clear(){
        tracks.clear()
        saveTracks()
    }
    override fun isEmpty() : Boolean{
        return tracks.isEmpty()
    }
    companion object{
        private const val SEARCH_HISTORY_LENGTH =10
    }
}