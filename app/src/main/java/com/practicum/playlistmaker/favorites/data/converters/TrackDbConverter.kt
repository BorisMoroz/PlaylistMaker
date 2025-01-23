package com.practicum.playlistmaker.favorites.data.converters

import com.practicum.playlistmaker.favorites.data.db.FavoriteTrackEntity
import com.practicum.playlistmaker.search.domain.models.Track
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TrackDbConverter {
    fun map(track: Track): FavoriteTrackEntity {
        val utcDateTime = LocalDateTime.now(ZoneOffset.UTC)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val timestamp = utcDateTime.format(formatter)

        return FavoriteTrackEntity(track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country,
            track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.previewUrl, timestamp)
    }

    fun map(track: FavoriteTrackEntity): Track{
        return Track(track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country,
            track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.previewUrl, true)
    }
}