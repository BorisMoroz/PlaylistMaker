package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.domain.models.DarkThemeState
import com.practicum.playlistmaker.sharing.domain.models.EmailData
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private val viewModel by viewModel<SettingsViewModel>()

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        viewModel.getDarkThemeState().observe(viewLifecycleOwner) { state ->
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