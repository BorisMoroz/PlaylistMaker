package com.practicum.playlistmaker.di.domain

import com.practicum.playlistmaker.favorites.data.repository.FavoriteTracksRepositoryImpl
import com.practicum.playlistmaker.favorites.domain.repository.FavoriteTracksRepository
import com.practicum.playlistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.practicum.playlistmaker.playlists.data.repository.PlayListsRepositoryImpl
import com.practicum.playlistmaker.playlists.domain.repository.PlaylistsRepository
import com.practicum.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import com.practicum.playlistmaker.settings.data.repository.AppPrefsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.repository.AppPrefsRepository
import com.practicum.playlistmaker.sharing.data.repository.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.repository.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get(), get())
    }

    single<AppPrefsRepository> {
        AppPrefsRepositoryImpl(get(), get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    single<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }

    single<PlaylistsRepository> {
        PlayListsRepositoryImpl(get(), get(), get())
    }
}