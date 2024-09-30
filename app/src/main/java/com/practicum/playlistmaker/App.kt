package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.data.repository.AppPrefsRepositoryImpl
import com.practicum.playlistmaker.domain.interactor.SwitchAppThemeInteractor

private const val PLAYLISTMAKER_PREFERENCES = "playlistmaker_preferences"

class App : Application() {
    lateinit var sharedPrefs : SharedPreferences
    lateinit var switchAppThemeInteractor : SwitchAppThemeInteractor
    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE)
        switchAppThemeInteractor = Creator.provideSwitchAppThemeInteractor(sharedPrefs)
    }
}

