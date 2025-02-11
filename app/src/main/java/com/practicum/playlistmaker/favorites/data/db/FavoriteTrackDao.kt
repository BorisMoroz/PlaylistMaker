package com.practicum.playlistmaker.favorites.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteTrackDao {
    @Insert(entity = FavoriteTrackEntity::class,  onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteTrackEntity)

    @Delete(entity = FavoriteTrackEntity::class)
    suspend fun deleteTrack(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_tracks_table ORDER BY timestamp DESC")
    suspend fun getTracks(): List<FavoriteTrackEntity>

    @Query("SELECT trackId FROM favorite_tracks_table")
    suspend fun getTracksIds(): List<Int>
}