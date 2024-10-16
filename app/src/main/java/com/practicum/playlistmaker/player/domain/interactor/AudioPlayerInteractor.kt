package com.practicum.playlistmaker.player.domain.interactor

interface AudioPlayerInteractor {
    fun preparePlayer(path : String)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun reset()
    fun release()
    fun getcurrentPosition() : Int
    fun setPlayBackCompleteCallBack(callBack : () -> Unit) {
    }
}