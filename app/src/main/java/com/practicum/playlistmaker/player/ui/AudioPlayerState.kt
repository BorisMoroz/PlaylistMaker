package com.practicum.playlistmaker.player.ui

sealed interface AudioPlayerState {
    data class Default(val time: String) : AudioPlayerState
    data class Prepared(val time: String) : AudioPlayerState
    data class Completed(val time: String) : AudioPlayerState
    data class Playing(val time: String) : AudioPlayerState
    data class Paused(val time : String) : AudioPlayerState
}
