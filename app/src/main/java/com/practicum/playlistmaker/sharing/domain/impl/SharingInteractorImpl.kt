package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.interactor.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.models.EmailData
import com.practicum.playlistmaker.sharing.domain.repository.ExternalNavigator

class SharingInteractorImpl(val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun shareApp(shareLink: String) {
        externalNavigator.shareLink(shareLink)
    }

    override fun userAgreement(openLink: String) {
        externalNavigator.openLink(openLink)
    }

    override fun openSupport(emailData: EmailData) {
        externalNavigator.openEmail(emailData)
    }
}
