package com.practicum.playlistmaker.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.data.dto.DarkThemeStateDto
import com.practicum.playlistmaker.domain.models.DarkThemeState
import com.practicum.playlistmaker.domain.repository.AppPrefsRepository

class AppPrefsRepositoryImpl(private val sharedPrefs : SharedPreferences) : AppPrefsRepository {
    val myGson = Gson()

    override fun saveDarkThemeState(darkThemeState : DarkThemeState){
        val nightThemeStateDto = DarkThemeStateDto(darkThemeState.state)
        val json = myGson.toJson(nightThemeStateDto)
        sharedPrefs.edit()
            .putString(DARK_THEME_STATE_KEY, json)
            .apply()
    }
    override fun restoreDarkThemeState() : DarkThemeState {
        val json = sharedPrefs.getString(DARK_THEME_STATE_KEY, null)
        if (json != null) {
            val darkThemeStateDto = myGson.fromJson(json, DarkThemeStateDto::class.java)

            return DarkThemeState(darkThemeStateDto.state)
        }
        else
            return DarkThemeState(false)
    }
    companion object{
        private const val DARK_THEME_STATE_KEY = "dark_theme_state"
    }
}