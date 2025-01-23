package com.practicum.playlistmaker.playlists.data.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.favorites.data.db.FavoriteTrackEntity
import com.practicum.playlistmaker.playlists.data.db.PlaylistEntity
import com.practicum.playlistmaker.playlists.domain.models.Playlist
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.domain.models.Track
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class PlaylistDbConverter(private val gson : Gson) {
    fun map(playlist: Playlist): PlaylistEntity {
        var tracksIdsJson = ""

        if(playlist.tracksIds.isNotEmpty()) {
            tracksIdsJson = gson.toJson(playlist.tracksIds)
        }
        return PlaylistEntity(id = playlist.id, title = playlist.title, description = playlist.description, cover = playlist.cover, tracksIds = tracksIdsJson, tracksNum = playlist.tracksNum)
    }

    fun map(playlist: PlaylistEntity): Playlist {
        var tracksIds: MutableList<Int> = mutableListOf()
        val tracksIdsJson = playlist.tracksIds

        if(tracksIdsJson.isNotEmpty()){
            val typeToken = object : TypeToken<List<Int>>() {}.type

            tracksIds = gson.fromJson(tracksIdsJson, typeToken)
        }
        return Playlist(playlist.id, playlist.title, playlist.description, playlist.cover, tracksIds, playlist.tracksNum)
    }
}

