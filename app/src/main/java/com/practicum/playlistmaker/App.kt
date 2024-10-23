package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.data.dataModule
import com.practicum.playlistmaker.di.domain.interactorModule
import com.practicum.playlistmaker.di.domain.repositoryModule
import com.practicum.playlistmaker.di.ui.viewModelModule
import com.practicum.playlistmaker.settings.domain.interactor.SwitchAppThemeInteractor
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        initApplicationTheme()
    }

    fun initApplicationTheme() {
        val switchAppThemeInteractor: SwitchAppThemeInteractor = getKoin().get()

        AppCompatDelegate.setDefaultNightMode(
            if (switchAppThemeInteractor.getCurrentDarkThemeState().state) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}

