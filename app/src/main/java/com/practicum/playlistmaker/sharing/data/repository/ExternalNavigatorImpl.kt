package com.practicum.playlistmaker.sharing.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.practicum.playlistmaker.sharing.domain.models.EmailData
import com.practicum.playlistmaker.sharing.domain.repository.ExternalNavigator

class ExternalNavigatorImpl(val context: Context) : ExternalNavigator {
    override fun shareLink(link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)

        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(context, shareIntent, null)
    }

    override fun openLink(link: String) {
        val userIntent = Intent(Intent.ACTION_VIEW)
        userIntent.data = Uri.parse(link)

        userIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(context, userIntent, null)
    }

    override fun openEmail(emailData: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, emailData.email)
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
        supportIntent.putExtra(Intent.EXTRA_TEXT, emailData.text)

        supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(context, supportIntent, null)
    }
}