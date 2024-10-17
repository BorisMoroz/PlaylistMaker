package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.interactor.SwitchAppThemeInteractor
import com.practicum.playlistmaker.settings.domain.models.DarkThemeState
import com.practicum.playlistmaker.settings.domain.repository.AppPrefsRepository

class SwitchAppThemeInteractorImpl(val repository: AppPrefsRepository) : SwitchAppThemeInteractor {
    override fun setCurrentDarkThemeState(darkThemeState: DarkThemeState) {
        repository.saveDarkThemeState(darkThemeState)
    }

    override fun getCurrentDarkThemeState(): DarkThemeState {
        return repository.restoreDarkThemeState()
    }
}
