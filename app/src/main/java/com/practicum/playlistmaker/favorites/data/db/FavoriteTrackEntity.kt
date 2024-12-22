package com.practicum.playlistmaker.favorites.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class FavoriteTrackEntity(
    @PrimaryKey
    val trackId : Int,
    val collectionName : String,
    val releaseDate : String,
    val primaryGenreName :String,
    val country : String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val previewUrl: String)





