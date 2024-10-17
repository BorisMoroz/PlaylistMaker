package com.practicum.playlistmaker.settings.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.settings.data.dto.DarkThemeStateDto
import com.practicum.playlistmaker.settings.domain.models.DarkThemeState
import com.practicum.playlistmaker.settings.domain.repository.AppPrefsRepository

class AppPrefsRepositoryImpl(private val sharedPrefs: SharedPreferences, private val gson: Gson) :
    AppPrefsRepository {
    override fun saveDarkThemeState(darkThemeState: DarkThemeState) {
        val nightThemeStateDto = DarkThemeStateDto(darkThemeState.state)
        val json = gson.toJson(nightThemeStateDto)
        sharedPrefs.edit()
            .putString(DARK_THEME_STATE_KEY, json)
            .apply()
    }

    override fun restoreDarkThemeState(): DarkThemeState {
        val json = sharedPrefs.getString(DARK_THEME_STATE_KEY, null)
        if (json != null) {
            val darkThemeStateDto = gson.fromJson(json, DarkThemeStateDto::class.java)

            return DarkThemeState(darkThemeStateDto.state)
        } else
            return DarkThemeState(false)
    }

    companion object {
        private const val DARK_THEME_STATE_KEY = "dark_theme_state"
    }
}