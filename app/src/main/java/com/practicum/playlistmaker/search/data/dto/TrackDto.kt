package com.practicum.playlistmaker.search.data.dto

data class TrackDto(val trackId : Int,
                    val collectionName : String,
                    val releaseDate : String,
                    val primaryGenreName :String,
                    val country : String,
                    val trackName: String,
                    val artistName: String, // Имя исполнителя
                    val trackTimeMillis: Long, // Продолжительность трека
                    val artworkUrl100: String, // Ссылка на изображение обложки
                    val previewUrl: String)
