package com.practicum.playlistmaker.presentation.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator.provideSettingsInteractor
import com.practicum.playlistmaker.domain.models.DarkThemeState

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val SettingsInteractor = provideSettingsInteractor(this)

        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<ImageView>(R.id.button_back)
        buttonBack.setOnClickListener {
            finish()
        }
        val buttonShare = findViewById<FrameLayout>(R.id.button_share)

        buttonShare.setOnClickListener {
            SettingsInteractor.share()
        }
        val buttonSupport = findViewById<FrameLayout>(R.id.button_support)
         buttonSupport.setOnClickListener {
             SettingsInteractor.support()
         }
        val buttonUser = findViewById<FrameLayout>(R.id.button_user)
        buttonUser.setOnClickListener {
            SettingsInteractor.userAgreement(/*this*/)
        }
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = (applicationContext as App).switchAppThemeInteractor.getDarkThemeState().state

        themeSwitcher.setOnCheckedChangeListener {switcher, checked ->
            (applicationContext as App).switchAppThemeInteractor.switchDarkTheme(DarkThemeState(checked))}
    }
}
