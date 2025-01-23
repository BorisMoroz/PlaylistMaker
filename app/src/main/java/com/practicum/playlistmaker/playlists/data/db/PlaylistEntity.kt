package com.practicum.playlistmaker.playlists.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.Nullable

@Entity(tableName = "playlists_table_test")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int /*= 0*/,
    val title : String,
    val description : String,
    val cover : String,
    val tracksIds : String,
    val tracksNum : Int)








