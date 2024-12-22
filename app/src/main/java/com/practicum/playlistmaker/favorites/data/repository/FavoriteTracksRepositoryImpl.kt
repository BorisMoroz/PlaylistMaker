package com.practicum.playlistmaker.favorites.data.repository

import com.practicum.playlistmaker.favorites.data.converters.TrackDbConverter
import com.practicum.playlistmaker.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.favorites.domain.repository.FavoriteTracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(private val appDatabase: AppDatabase, private val trackDbConverter: TrackDbConverter) :
    FavoriteTracksRepository {

    override suspend fun addTrack(track: Track){

        appDatabase.favoriteTrackDao().insertTrack(trackDbConverter.map(track))




    }


    override suspend fun deleteTrack(trackId : Int){

        appDatabase.favoriteTrackDao().deleteTrack(trackId)



    }


    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.favoriteTrackDao().getTracks()
        emit(tracks.map{track -> trackDbConverter.map(track)})
    }










}