package com.practicum.playlistmaker.player.domain.repository

interface AudioPlayerRepository {
    fun preparePlayer(path : String)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun reset()
    fun release()
    fun getcurrentPosition() : Int

    fun setPlayBackCompleteCallBack(callBack : () -> Unit)
}