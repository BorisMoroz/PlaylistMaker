package com.practicum.playlistmaker.domain.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.interactor.SettingsInteractor

class SettingsInteractorImpl(val context: Context) : SettingsInteractor {
    override fun share(/*context : Context*/){
        val message = context.getString(R.string.settings_share_url)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(context, shareIntent, null)
    }
    override fun support(/*context : Context*/){
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.settings_support_address)))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.settings_support_subject))
        supportIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.settings_support_message))
        startActivity(context, supportIntent, null)
    }
    override fun userAgreement(/*context: Context*/){
        val userIntent = Intent(Intent.ACTION_VIEW)
        val url =  context.getString(R.string.settings_user_url)
        userIntent.data = Uri.parse(url)
        startActivity(context, userIntent, null)
    }
}
