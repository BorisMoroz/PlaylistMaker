package com.practicum.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.ui.choosedTrack
import java.util.Locale

class AudioPleerViewModel(/*private val context: Context*/) : ViewModel() {
    private val AudioPlayerInteractor = Creator.provideAudioPlayerInteractor()
    private val handler = Handler(Looper.getMainLooper())

    private val audioPlayerState =
        MutableLiveData<AudioPlayerState>(AudioPlayerState.Default(formatTime(0)))

    init {
        preparePlayer(choosedTrack.previewUrl)
        setPlayBackCompleteCallBack {
            onComplete()
        }
    }

    fun getAudioPlayerState(): LiveData<AudioPlayerState> = audioPlayerState

    fun preparePlayer(path: String) {
        AudioPlayerInteractor.preparePlayer(path)
        audioPlayerState.value = AudioPlayerState.Prepared(formatTime(0))
    }
    fun startPlayer() {
        setTimerOn()
        AudioPlayerInteractor.startPlayer()
    }
    fun pausePlayer() {
        AudioPlayerInteractor.pausePlayer()
        setTimerOff()
        audioPlayerState.value = AudioPlayerState.Paused(formatTime(getcurrentPosition()))
    }
    fun reset() {
        AudioPlayerInteractor.reset()
        setTimerOff()
        AudioPlayerState.Prepared(formatTime(0))
    }
    fun release() {
        AudioPlayerInteractor.release()
    }
    fun getcurrentPosition(): Int {
        return AudioPlayerInteractor.getcurrentPosition()
    }
    fun setPlayBackCompleteCallBack(callBack: () -> Unit) {
        AudioPlayerInteractor.setPlayBackCompleteCallBack(callBack)
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            audioPlayerState.value = AudioPlayerState.Playing(formatTime(getcurrentPosition()))
            handler.postDelayed(this, TIMER_STEP)
        }
    }
    private fun setTimerOn() {
        handler.post(timerRunnable)
    }
    private fun setTimerOff() {
        handler.removeCallbacks(timerRunnable)
    }
    private fun onComplete() {
        setTimerOff()
        audioPlayerState.value = AudioPlayerState.Completed(formatTime(0))
    }
    fun formatTime(time: Int): String {
        return (SimpleDateFormat("mm:ss", Locale.getDefault()).format(time))
    }
    override fun onCleared() {
        AudioPlayerInteractor.reset()
    }
    companion object {
        private const val TIMER_STEP = 500L
    }
}








