package com.practicum.playlistmaker.playlists.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface PlaylistsTrackDao {
    @Insert(entity = PlaylistsTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistsTrack(playlistsTrack: PlaylistsTrackEntity)
}
