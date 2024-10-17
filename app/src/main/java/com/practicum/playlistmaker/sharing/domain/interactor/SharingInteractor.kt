package com.practicum.playlistmaker.sharing.domain.interactor

import com.practicum.playlistmaker.sharing.domain.models.EmailData

interface SharingInteractor {
    fun shareApp(shareLink: String)
    fun openSupport(emailData: EmailData)
    fun userAgreement(openLink: String)
}

