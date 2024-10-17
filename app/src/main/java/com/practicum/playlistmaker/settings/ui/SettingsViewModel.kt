package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.models.DarkThemeState
import com.practicum.playlistmaker.sharing.domain.models.EmailData

class SettingsViewModel : ViewModel() {
    private val SharingInteractor = Creator.provideSharingInteractor()
    private val SwitchAppThemeInteractor = Creator.provideSwitchAppThemeInteractor()

    private val darkThemeState = MutableLiveData<DarkThemeState>()

    init {
        darkThemeState.value = getCurrentDarkThemeState()
    }

    fun getDarkThemeState(): LiveData<DarkThemeState> = darkThemeState

    fun setCurrentDarkThemeState(darkThemeState: DarkThemeState) {
        SwitchAppThemeInteractor.setCurrentDarkThemeState(darkThemeState)
        this.darkThemeState.value = darkThemeState
    }

    fun getCurrentDarkThemeState(): DarkThemeState {
        return SwitchAppThemeInteractor.getCurrentDarkThemeState()
    }

    fun shareApp(shareLink: String) {
        SharingInteractor.shareApp(shareLink)
    }

    fun openSupport(emailData: EmailData) {
        SharingInteractor.openSupport(emailData)
    }

    fun userAgreement(openLink: String) {
        SharingInteractor.userAgreement(openLink)
    }
}