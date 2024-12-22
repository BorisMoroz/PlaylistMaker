package com.practicum.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.favorites.domain.interactor.FavoriteTracksInteractor
import com.practicum.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.search.ui.choosedTrack
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class AudioPleerViewModel(val audioPlayerInteractor : AudioPlayerInteractor, val favoriteTracksInteractor : FavoriteTracksInteractor) : ViewModel() {
    private var timerJob: Job? = null

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
        audioPlayerInteractor.preparePlayer(path)
        audioPlayerState.value = AudioPlayerState.Prepared(formatTime(0))
    }
    fun startPlayer() {
        setTimerOn()
        audioPlayerInteractor.startPlayer()
    }
    fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        setTimerOff()
        audioPlayerState.value = AudioPlayerState.Paused(formatTime(getcurrentPosition()))
    }
    fun reset() {
        audioPlayerInteractor.reset()
        setTimerOff()
        AudioPlayerState.Prepared(formatTime(0))
    }
    fun release() {
        audioPlayerInteractor.release()
    }
    fun getcurrentPosition(): Int {
        return audioPlayerInteractor.getcurrentPosition()
    }
    fun setPlayBackCompleteCallBack(callBack: () -> Unit) {
        audioPlayerInteractor.setPlayBackCompleteCallBack(callBack)
    }

    private fun setTimerOn() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(TIMER_STEP)
                audioPlayerState.value = AudioPlayerState.Playing(formatTime(getcurrentPosition()))
            }
        }
    }
    private fun setTimerOff() {
        timerJob?.cancel()
    }
    private fun onComplete() {
        setTimerOff()
        audioPlayerState.value = AudioPlayerState.Completed(formatTime(0))
    }
    fun formatTime(time: Int): String {
        return (SimpleDateFormat("mm:ss", Locale.getDefault()).format(time))
    }
    override fun onCleared() {
        audioPlayerInteractor.reset()
    }


    fun changeTrackFavoriteState(){
        if(!choosedTrack.isFavorite){

            viewModelScope.launch{favoriteTracksInteractor.addTrack(choosedTrack)}





        }
        else {

            viewModelScope.launch{favoriteTracksInteractor.deleteTrack(choosedTrack.trackId)}



        }

        choosedTrack.isFavorite = !choosedTrack.isFavorite

        audioPlayerState.value = audioPlayerState.value


    }




    companion object {
        private const val TIMER_STEP = 300L
    }
}








