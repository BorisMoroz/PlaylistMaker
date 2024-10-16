package com.practicum.playlistmaker.settings.domain.repository

import com.practicum.playlistmaker.settings.domain.models.DarkThemeState

interface AppPrefsRepository{
    fun saveDarkThemeState(darkThemeState : DarkThemeState)
    fun restoreDarkThemeState() : DarkThemeState
}