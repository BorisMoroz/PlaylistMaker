package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.models.Resource
import com.practicum.playlistmaker.domain.repository.TracksRepository
import com.practicum.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            return Resource.Success((response as TracksSearchResponse).results.map {
                Track(it.trackId, it.collectionName, it.releaseDate, it.primaryGenreName, it.country,
                    it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.previewUrl)})
        } else {
            return Resource.Error("Произошла сетевая ошибка")
        }
    }
}
