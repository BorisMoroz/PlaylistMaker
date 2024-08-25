package com.practicum.playlistmaker

data class Track(
    val trackId : Int,
    val trackName: String,
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String // Ссылка на изображение обложки
)
