package com.practicum.playlistmaker.di.ui

import com.practicum.playlistmaker.player.ui.AudioPleerViewModel
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AudioPleerViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }
}