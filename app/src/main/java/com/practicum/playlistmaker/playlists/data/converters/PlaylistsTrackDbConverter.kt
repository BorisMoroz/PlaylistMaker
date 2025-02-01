package com.practicum.playlistmaker.playlists.data.converters

import com.google.gson.Gson
import com.practicum.playlistmaker.favorites.data.db.FavoriteTrackEntity
import com.practicum.playlistmaker.playlists.data.db.PlaylistsTrackEntity
import com.practicum.playlistmaker.search.domain.models.Track
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class PlaylistsTrackDbConverter(private val gson : Gson) {
    fun map(track: Track): PlaylistsTrackEntity {
        /*val utcDateTime = LocalDateTime.now(ZoneOffset.UTC)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val timestamp = utcDateTime.format(formatter)*/

        return PlaylistsTrackEntity(track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country,
            track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.previewUrl)
    }

    fun map(track: PlaylistsTrackEntity): Track {
        return Track(track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country,
            track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.previewUrl, false)
    }
}