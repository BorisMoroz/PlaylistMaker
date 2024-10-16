package com.practicum.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.settings.data.repository.AppPrefsRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.search.domain.interactor.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.sharing.domain.interactor.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.practicum.playlistmaker.settings.domain.interactor.SwitchAppThemeInteractor
import com.practicum.playlistmaker.settings.domain.impl.SwitchAppThemeInteractorImpl
import com.practicum.playlistmaker.settings.domain.repository.AppPrefsRepository
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import com.practicum.playlistmaker.search.domain.use_case.SearchTracksUseCase
import com.practicum.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.practicum.playlistmaker.sharing.data.repository.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.repository.ExternalNavigator

object Creator {
    lateinit var application : Application
    lateinit var sharedPrefs: SharedPreferences

    val gson = Gson()
    var mediaPlayer = MediaPlayer()

    fun saveAppliation(app : Application){
        application = app
    }
    fun saveSharedPrefs(sharedPrefs : SharedPreferences){
        this.sharedPrefs = sharedPrefs
    }

    fun provideSearchTracksUseCase(): SearchTracksUseCase {
        return SearchTracksUseCase(provideTracksRepository())
    }
    private fun provideTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(provideNetworkClient())
    }
    private fun provideNetworkClient(): NetworkClient {
        return RetrofitNetworkClient()
    }
    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistoryRepository())
    }
    private fun provideSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPrefs, gson)
    }
    fun provideSharingInteractor() : SharingInteractor {
        return SharingInteractorImpl(provideExternalNavigator())
    }
    private fun provideExternalNavigator() : ExternalNavigatorImpl {
        return ExternalNavigatorImpl(application)
    }
    fun provideSwitchAppThemeInteractor(): SwitchAppThemeInteractor {
        return SwitchAppThemeInteractorImpl(provideAppPrefRepository())
    }
    private fun provideAppPrefRepository(): AppPrefsRepository {
        return AppPrefsRepositoryImpl(sharedPrefs, gson)
    }
    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(provideAudioPlayerRepository())
    }
    private fun provideAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(mediaPlayer)
    }
}