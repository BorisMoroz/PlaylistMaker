package com.practicum.playlistmaker.playlists.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.favorites.data.db.FavoriteTrackEntity

@Dao
interface PlaylistsTrackDao {
    @Insert(entity = PlaylistsTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistsTrack(playlistsTrack: PlaylistsTrackEntity)



    @Delete(entity = PlaylistsTrackEntity::class)
    suspend fun deletePlaylistsTrack(track: PlaylistsTrackEntity)





    @Query("SELECT * FROM playlists_tracks_table")
    suspend fun getAllTracks(): List<PlaylistsTrackEntity>






}
