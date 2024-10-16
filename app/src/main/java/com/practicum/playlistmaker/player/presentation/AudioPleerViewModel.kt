package com.practicum.playlistmaker.player.presentation

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.creator.Creator

class AudioPleerViewModel : ViewModel() {
    private val AudioPlayerInteractor = Creator.provideAudioPlayerInteractor()

    fun preparePlayer(path : String){
        AudioPlayerInteractor.preparePlayer(path)
    }
    fun startPlayer(){
        AudioPlayerInteractor.startPlayer()
    }
    fun pausePlayer(){
        AudioPlayerInteractor.pausePlayer()
    }
    fun playbackControl(){
        AudioPlayerInteractor.playbackControl()
    }
    fun reset(){
        AudioPlayerInteractor.reset()
    }
    fun release(){
        AudioPlayerInteractor.release()
    }
    fun getcurrentPosition() : Int{
        return AudioPlayerInteractor.getcurrentPosition()
    }

    fun setPlayBackCompleteCallBack(callBack : () -> Unit) {
        AudioPlayerInteractor.setPlayBackCompleteCallBack(callBack)
    }
}








