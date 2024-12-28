package com.practicum.playlistmaker.favorites.data.converters

import com.practicum.playlistmaker.favorites.data.db.FavoriteTrackEntity
import com.practicum.playlistmaker.search.domain.models.Track

class TrackDbConverter {
    fun map(track: Track): FavoriteTrackEntity{
        return FavoriteTrackEntity(track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country,
            track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.previewUrl)
    }

    fun map(track: FavoriteTrackEntity): Track{
        return Track(track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country,
            track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.previewUrl, true)
    }
}