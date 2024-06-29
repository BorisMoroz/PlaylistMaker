package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<ImageView>(R.id.button_back)

        buttonBack.setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent)}

        val buttonShare = findViewById<FrameLayout>(R.id.button_share)

        buttonShare.setOnClickListener {
            val message = getString(R.string.settings_share_url)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

        val buttonSupport = findViewById<FrameLayout>(R.id.button_support)

         buttonSupport.setOnClickListener {
             val message =  getString(R.string.settings_support_message)
             val shareIntent = Intent(Intent.ACTION_SENDTO)
             shareIntent.data = Uri.parse("mailto:")
             shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.settings_support_address)))
             shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_support_subject))
             shareIntent.putExtra(Intent.EXTRA_TEXT, message)
             startActivity(shareIntent)
         }

        val buttonUser = findViewById<FrameLayout>(R.id.button_user)

        buttonUser.setOnClickListener {
            val userIntent = Intent(Intent.ACTION_VIEW)
            val url =  getString(R.string.settings_user_url)
            userIntent.data = Uri.parse(url)
            startActivity(userIntent)
        }
    }
}