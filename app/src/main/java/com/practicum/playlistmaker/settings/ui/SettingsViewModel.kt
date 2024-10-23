package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.interactor.SwitchAppThemeInteractor
import com.practicum.playlistmaker.settings.domain.models.DarkThemeState
import com.practicum.playlistmaker.sharing.domain.interactor.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.models.EmailData

class SettingsViewModel(val sharingInteractor : SharingInteractor, val switchAppThemeInteractor : SwitchAppThemeInteractor) : ViewModel() {
    private val darkThemeState = MutableLiveData<DarkThemeState>()

    init {
        darkThemeState.value = getCurrentDarkThemeState()
    }

    fun getDarkThemeState(): LiveData<DarkThemeState> = darkThemeState

    fun setCurrentDarkThemeState(darkThemeState: DarkThemeState) {
        switchAppThemeInteractor.setCurrentDarkThemeState(darkThemeState)
        this.darkThemeState.value = darkThemeState
    }

    fun getCurrentDarkThemeState(): DarkThemeState {
        return switchAppThemeInteractor.getCurrentDarkThemeState()
    }

    fun shareApp(shareLink: String) {
        sharingInteractor.shareApp(shareLink)
    }

    fun openSupport(emailData: EmailData) {
        sharingInteractor.openSupport(emailData)
    }

    fun userAgreement(openLink: String) {
        sharingInteractor.userAgreement(openLink)
    }
}