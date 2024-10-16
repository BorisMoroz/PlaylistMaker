package com.practicum.playlistmaker.settings.domain.interactor

import com.practicum.playlistmaker.settings.domain.models.DarkThemeState

interface SwitchAppThemeInteractor {
    fun setCurrentDarkThemeState(darkThemeState : DarkThemeState)
    fun getCurrentDarkThemeState() : DarkThemeState
}