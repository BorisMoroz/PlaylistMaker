package com.practicum.playlistmaker.playlists.domain.models

data class Playlist(
    var id : Int,
    val title : String,
    val description : String,
    val cover : String,
    val tracksIds : MutableList<Int>,
    var tracksNum : Int
)
