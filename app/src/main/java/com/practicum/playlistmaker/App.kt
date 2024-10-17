package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator

private const val PLAYLISTMAKER_PREFERENCES = "playlistmaker_preferences"

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Creator.saveAppliation(this)
        initApplicationTheme()
    }

    fun initApplicationTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (Creator.provideSwitchAppThemeInteractor().getCurrentDarkThemeState().state) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}

