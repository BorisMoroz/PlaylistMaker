package com.practicum.playlistmaker

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.interactor.SwitchAppThemeInteractor

private const val PLAYLISTMAKER_PREFERENCES = "playlistmaker_preferences"

class App : Application() {
        override fun onCreate() {
        super.onCreate()

        Creator.saveAppliation(this)
        Creator.saveSharedPrefs(getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE))

        initApplicationTheme()
    }
    fun initApplicationTheme(){
        AppCompatDelegate.setDefaultNightMode(
            if (Creator.provideSwitchAppThemeInteractor().getCurrentDarkThemeState().state) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}

