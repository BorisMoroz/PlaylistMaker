package com.practicum.playlistmaker.domain.impl

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.domain.interactor.SwitchAppThemeInteractor
import com.practicum.playlistmaker.domain.models.DarkThemeState
import com.practicum.playlistmaker.domain.repository.AppPrefsRepository

class SwitchAppThemeInteractorImpl(val repository : AppPrefsRepository) : SwitchAppThemeInteractor {
    var darkTheme = DarkThemeState(false)

    init {
        switchDarkTheme(repository.restoreDarkThemeState())
    }

    override fun
            getDarkThemeState() : DarkThemeState{
        return darkTheme
    }
    override fun switchDarkTheme(darkThemeState: DarkThemeState){
          darkTheme = darkThemeState

          AppCompatDelegate.setDefaultNightMode(
              if (darkThemeState.state) {
                  AppCompatDelegate.MODE_NIGHT_YES
              } else {
                  AppCompatDelegate.MODE_NIGHT_NO
              }
          )
          repository.saveDarkThemeState(darkThemeState)
      }
}
