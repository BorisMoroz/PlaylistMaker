package com.practicum.playlistmaker.domain.interactor

interface AudioPlayerInteractor {
    fun preparePlayer(path : String)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun release()
    fun getcurrentPosition() : Int

    fun setPlayBackCompleteCallBack(callBack : () -> Unit) {
    }
}