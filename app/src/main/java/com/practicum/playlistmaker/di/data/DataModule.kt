package com.practicum.playlistmaker.di.data

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.favorites.data.converters.TrackDbConverter
import com.practicum.playlistmaker.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.playlists.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.playlists.data.converters.PlaylistsTrackDbConverter
import com.practicum.playlistmaker.search.data.network.ITunesApi
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val iTunesBaseUrl = "https://itunes.apple.com"
private const val PLAYLISTMAKER_PREFERENCES = "playlistmaker_preferences"

val dataModule = module {

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "playlistmaker_database_test_3.db")
            .build()
    }

    factory { TrackDbConverter() }

    factory { PlaylistDbConverter(get()) }

    factory { PlaylistsTrackDbConverter(get()) }

    single {
        androidContext().getSharedPreferences(PLAYLISTMAKER_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single { Gson() }

    single { MediaPlayer() }
}




