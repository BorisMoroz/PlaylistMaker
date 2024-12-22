package com.practicum.playlistmaker.favorites.domain.interactor

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {

    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(trackId : Int)

    fun getTracks(): Flow<List<Track>>



}