package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.repository.AudioPlayerRepository

class AudioPlayerInteractorImpl(private val repository: AudioPlayerRepository) :
    AudioPlayerInteractor {
    override fun preparePlayer(path : String){
        repository.preparePlayer(path)
    }
    override fun startPlayer(){
        repository.startPlayer()
    }
    override fun pausePlayer(){
        repository.pausePlayer()
    }
    override fun playbackControl(){
        repository.playbackControl()
    }
    override fun release() {
        repository.release()
    }
    override fun getcurrentPosition() : Int{
        return repository.getcurrentPosition()
    }

    override fun setPlayBackCompleteCallBack(callBack : () -> Unit) {
        repository.setPlayBackCompleteCallBack(callBack)
    }
}