package com.practicum.playlistmaker.domain.repository

interface AudioPlayerRepository {
    fun preparePlayer(path : String)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun release()
    fun getcurrentPosition() : Int

    fun setPlayBackCompleteCallBack(callBack : () -> Unit)
}