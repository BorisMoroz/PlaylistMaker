package com.practicum.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.AppPrefsRepositoryImpl
import com.practicum.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.domain.impl.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.domain.interactor.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.interactor.SettingsInteractor
import com.practicum.playlistmaker.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.interactor.SwitchAppThemeInteractor
import com.practicum.playlistmaker.domain.impl.SwitchAppThemeInteractorImpl
import com.practicum.playlistmaker.domain.repository.AppPrefsRepository
import com.practicum.playlistmaker.domain.repository.SearchHistoryRepository
import com.practicum.playlistmaker.domain.repository.TracksRepository
import com.practicum.playlistmaker.domain.use_case.SearchTracksUseCase
import com.practicum.playlistmaker.presentation.ui.player.AudioPlayerInteractor
import com.practicum.playlistmaker.presentation.ui.player.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.presentation.ui.player.AudioPlayerRepository

object Creator {
    val gson = Gson()
    fun provideSearchTracksUseCase(): SearchTracksUseCase {
        return SearchTracksUseCase(provideTracksRepository())
    }
    private fun provideTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(provideNetworkClient())
    }
    private fun provideNetworkClient(): NetworkClient {
        return RetrofitNetworkClient()
    }

    fun provideSearchHistoryInteractor(sharedPrefs : SharedPreferences): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistoryRepository(sharedPrefs))
    }
    private fun provideSearchHistoryRepository(sharedPrefs : SharedPreferences): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPrefs, gson)
    }

    fun provideSettingsInteractor(context: Context) : SettingsInteractor {
        return SettingsInteractorImpl(context)
    }

    fun provideSwitchAppThemeInteractor(sharedPrefs : SharedPreferences): SwitchAppThemeInteractor {
        return SwitchAppThemeInteractorImpl(provideAppPrefRepository(sharedPrefs))
    }
    private fun provideAppPrefRepository(sharedPrefs : SharedPreferences): AppPrefsRepository {
        return AppPrefsRepositoryImpl(sharedPrefs)
    }
    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(provideAudioPlayerRepository())
    }
    private fun provideAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl()
    }
}