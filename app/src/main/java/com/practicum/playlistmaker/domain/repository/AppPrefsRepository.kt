package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.DarkThemeState

interface AppPrefsRepository{
    fun saveDarkThemeState(darkThemeState : DarkThemeState)
    fun restoreDarkThemeState() : DarkThemeState
}