package com.practicum.playlistmaker.search.domain.models

data class Track(
    val trackId : Int,
    val collectionName : String,
    val releaseDate : String,
    val primaryGenreName :String,
    val country : String,
    val trackName: String,
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val previewUrl: String,

    var isFavorite: Boolean = false

) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    fun getReleaseYear() = releaseDate.substring(0,releaseDate.indexOf("-"))
}
