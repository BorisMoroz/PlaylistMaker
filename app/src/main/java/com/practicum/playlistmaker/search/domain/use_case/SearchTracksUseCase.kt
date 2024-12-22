package com.practicum.playlistmaker.search.domain.use_case

import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executors

class SearchTracksUseCase(private val repository: TracksRepository) {
    fun execute(text: String): Flow<Resource<List<Track>>> {
        return repository.searchTracks(text)
    }
}