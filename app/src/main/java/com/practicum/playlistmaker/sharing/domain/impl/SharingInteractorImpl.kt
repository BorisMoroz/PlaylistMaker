package com.practicum.playlistmaker.sharing.domain.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.interactor.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.models.EmailData
import com.practicum.playlistmaker.sharing.domain.repository.ExternalNavigator
class SharingInteractorImpl(val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun shareApp(shareLink : String) {
        externalNavigator.shareLink(shareLink)
    }
    override fun userAgreement(openLink : String) {
        externalNavigator.openLink(openLink)
    }
    override fun openSupport(emailData : EmailData) {
        externalNavigator.openEmail(emailData)
    }
}
