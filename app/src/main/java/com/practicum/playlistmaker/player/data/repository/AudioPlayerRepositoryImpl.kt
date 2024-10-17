package com.practicum.playlistmaker.player.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.repository.AudioPlayerRepository

class AudioPlayerRepositoryImpl(val mediaPlayer: MediaPlayer) : AudioPlayerRepository {
        private var playerState = STATE_DEFAULT

    override fun preparePlayer(path : String){
        mediaPlayer.setDataSource(path)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }
    override fun startPlayer(){
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }
    override fun pausePlayer(){
        if (playerState == STATE_PLAYING){
           mediaPlayer.pause()
           playerState = STATE_PAUSED
        }
    }
    override fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
   }
    override fun reset(){
        mediaPlayer.reset()
    }
    override fun release(){
        mediaPlayer.release()
    }
    override fun getcurrentPosition() : Int{
        return mediaPlayer.currentPosition
    }

    override fun setPlayBackCompleteCallBack(callBack : () -> Unit) {
        mediaPlayer.setOnCompletionListener { callBack()
        playerState = STATE_PREPARED
        }
    }
companion object {
    private const val STATE_DEFAULT = 0
    private const val STATE_PREPARED = 1
    private const val STATE_PLAYING = 2
    private const val STATE_PAUSED = 3
    }
}