package com.practicum.playlistmaker.playlists.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_tracks_table")
data class PlaylistsTrackEntity(
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
