package com.practicum.playlistmaker.presentation.ui.player

interface AudioPlayerRepository {
    fun preparePlayer(path : String)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun release()
    fun getcurrentPosition() : Int

    fun setPlayBackCompleteCallBack(callBack : () -> Unit)
}