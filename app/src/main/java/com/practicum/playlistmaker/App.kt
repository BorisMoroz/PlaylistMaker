package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

private const val PLAYLISTMAKER_PREFERENCES = "playlistmaker_preferences"
private const val NIGHT_THEME_STATE_KEY = "night_theme_state"

class App : Application() {
    var darkTheme = false

    lateinit var sharedPrefs : SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(PLAYLISTMAKER_PREFERENCES, MODE_PRIVATE)
        darkTheme = restoreNightThemeState()
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeState: Boolean) {
        darkTheme = darkThemeState
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeState) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        saveNightThemeState(darkThemeState)
    }

    fun saveNightThemeState(darkThemeState : Boolean) {
        sharedPrefs.edit()
            .putBoolean(NIGHT_THEME_STATE_KEY, darkThemeState)
            .apply()
    }

    fun restoreNightThemeState() : Boolean {
        return sharedPrefs.getBoolean(NIGHT_THEME_STATE_KEY, false)
    }
}

