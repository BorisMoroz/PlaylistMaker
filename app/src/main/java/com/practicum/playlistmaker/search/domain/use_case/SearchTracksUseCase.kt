package com.practicum.playlistmaker.search.domain.use_case

import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import java.util.concurrent.Executors

class SearchTracksUseCase(private val repository: TracksRepository) {
    private val executor = Executors.newCachedThreadPool()

    fun execute(text: String, consumer: Consumer<List<Track>>){
        executor.execute {
            val searchResponse = repository.searchTracks(text)

            when (searchResponse) {
               is Resource.Success -> {
                   consumer.consume(ConsumerData.Data(searchResponse.data))
               }
               is Resource.Error -> {
                   consumer.consume(ConsumerData.Error("Произошла сетевая ошибка"))
               }
            }
        }
    }
}