package com.practicum.playlistmaker.domain.interactor

import com.practicum.playlistmaker.domain.models.DarkThemeState

interface SwitchAppThemeInteractor {
    fun getDarkThemeState() : DarkThemeState
    fun switchDarkTheme(darkThemeState: DarkThemeState)
}