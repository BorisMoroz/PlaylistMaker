package com.practicum.playlistmaker.sharing.domain.repository

import com.practicum.playlistmaker.sharing.domain.models.EmailData

interface ExternalNavigator {
    fun shareLink(link: String)
    fun openLink(link: String)
    fun openEmail(emailData: EmailData)
}