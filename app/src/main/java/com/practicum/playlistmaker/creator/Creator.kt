package com.practicum.playlistmaker.creator

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.interactor.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import com.practicum.playlistmaker.search.domain.use_case.SearchTracksUseCase
import com.practicum.playlistmaker.settings.data.repository.AppPrefsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.impl.SwitchAppThemeInteractorImpl
import com.practicum.playlistmaker.settings.domain.interactor.SwitchAppThemeInteractor
import com.practicum.playlistmaker.settings.domain.repository.AppPrefsRepository
import com.practicum.playlistmaker.sharing.data.repository.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.practicum.playlistmaker.sharing.domain.interactor.SharingInteractor

object Creator {

}