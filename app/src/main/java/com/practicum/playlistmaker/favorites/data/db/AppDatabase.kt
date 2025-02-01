package com.practicum.playlistmaker.favorites.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.playlists.data.db.PlaylistDao
import com.practicum.playlistmaker.playlists.data.db.PlaylistEntity
import com.practicum.playlistmaker.playlists.data.db.PlaylistsTrackDao
import com.practicum.playlistmaker.playlists.data.db.PlaylistsTrackEntity

@Database(version = 1, entities = [FavoriteTrackEntity::class, PlaylistEntity::class, PlaylistsTrackEntity::class])
abstract class AppDatabase: RoomDatabase(){
    abstract fun favoriteTrackDao(): FavoriteTrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistsTrackDao(): PlaylistsTrackDao
}