package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            emit(Resource.Success((response as TracksSearchResponse).results.map {
                Track(it.trackId, it.collectionName, it.releaseDate, it.primaryGenreName, it.country,
                    it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.previewUrl)
            }))
        } else {
            emit(Resource.Error("Произошла сетевая ошибка"))
        }
    }
}
