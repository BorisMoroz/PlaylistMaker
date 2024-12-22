package com.practicum.playlistmaker.favorites.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteTrackEntity)

    @Delete
    suspend fun deleteTrack(trackId: Int)

    @Query("SELECT * FROM track_table")
    suspend fun getTracks(): List<FavoriteTrackEntity>

    @Query("SELECT trackId FROM track_table")
    suspend fun getTracksIds(): List<Int>
}