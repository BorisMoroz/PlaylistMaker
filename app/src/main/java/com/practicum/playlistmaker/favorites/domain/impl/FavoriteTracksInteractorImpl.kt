package com.practicum.playlistmaker.favorites.domain.impl

import com.practicum.playlistmaker.favorites.data.repository.FavoriteTracksRepositoryImpl
import com.practicum.playlistmaker.favorites.domain.interactor.FavoriteTracksInteractor
import com.practicum.playlistmaker.favorites.domain.repository.FavoriteTracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class FavoriteTracksInteractorImpl(private val favoriteTracksRepository: FavoriteTracksRepository) : FavoriteTracksInteractor {

    override suspend fun addTrack(track: Track){

        favoriteTracksRepository.addTrack(track)

    }

    override suspend fun deleteTrack(trackId : Int){

        favoriteTracksRepository.deleteTrack(trackId)

    }




    override fun getTracks(): Flow<List<Track>>{

        return favoriteTracksRepository.getTracks().map{tracks -> tracks.reversed()}




    }




}