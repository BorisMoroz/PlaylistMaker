package com.practicum.playlistmaker.di.domain

import com.practicum.playlistmaker.favorites.domain.impl.FavoriteTracksInteractorImpl
import com.practicum.playlistmaker.favorites.domain.interactor.FavoriteTracksInteractor
import com.practicum.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.interactor.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.use_case.SearchTracksUseCase
import com.practicum.playlistmaker.settings.domain.impl.SwitchAppThemeInteractorImpl
import com.practicum.playlistmaker.settings.domain.interactor.SwitchAppThemeInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.practicum.playlistmaker.sharing.domain.interactor.SharingInteractor
import org.koin.dsl.module

val interactorModule = module {
    single<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single {
        SearchTracksUseCase(get())
    }

    single<SwitchAppThemeInteractor> {
        SwitchAppThemeInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }
}