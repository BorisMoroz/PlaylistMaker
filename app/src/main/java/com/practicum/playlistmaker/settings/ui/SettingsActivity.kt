package com.practicum.playlistmaker.settings.ui

//import com.practicum.playlistmaker.creator.Creator.provideSettingsInteractor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.domain.models.DarkThemeState
import com.practicum.playlistmaker.sharing.domain.models.EmailData

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel
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
            viewModel.userAgreement(getString(R.string.settings_user_url))
        }

        binding.buttonSupport.setOnClickListener {
            viewModel.openSupport(
                EmailData(
                    arrayOf(getString(R.string.settings_support_address)),
                    getString(R.string.settings_support_subject),
                    getString(R.string.settings_support_message)
                )
            )
        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.setCurrentDarkThemeState(DarkThemeState(checked))
        }

        viewModel.getDarkThemeState().observe(this) { state ->
            renderDarkThemeState(state)
        }
    }

    fun renderDarkThemeState(state: DarkThemeState) {
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
