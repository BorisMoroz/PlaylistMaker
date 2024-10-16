package com.practicum.playlistmaker.settings.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
//import com.practicum.playlistmaker.creator.Creator.provideSettingsInteractor
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.domain.models.DarkThemeState
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel

import androidx.core.content.ContextCompat.startActivity
import com.practicum.playlistmaker.sharing.data.repository.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.models.EmailData

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel : SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.buttonShare.setOnClickListener {
            viewModel.shareApp(getString(R.string.settings_share_url))
        }

        binding.buttonUser.setOnClickListener {
            viewModel.userAgreement(getString(R.string.settings_user_url)) }

        binding.buttonSupport.setOnClickListener {
            viewModel.openSupport(EmailData(arrayOf(getString(R.string.settings_support_address)),
                getString(R.string.settings_support_subject),
                getString(R.string.settings_support_message) ))}

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.setCurrentDarkThemeState(DarkThemeState(checked))
        }

        viewModel.getDarkThemeState().observe(this) { state ->
            renderDarkThemeState(state)
        }
    }
    fun renderDarkThemeState(state : DarkThemeState){
        AppCompatDelegate.setDefaultNightMode(
            if (state.state) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        binding.themeSwitcher.isChecked = state.state
    }
}
