package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val iTunesApi: ITunesApi) : NetworkClient {
    /*private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesApi = retrofit.create(ITunesApi::class.java) */

    override fun doRequest(dto: Any): Response {
        try {
            if (dto is TracksSearchRequest) {
                val resp = iTunesApi.findSong(dto.text).execute()
                val body = resp.body() ?: Response()

                return body.apply { resultCode = resp.code() }
            } else {
                return Response().apply { resultCode = 400 }
            }
        }
        catch (ex : Exception){
            return Response().apply { resultCode = 400 }
        }
    }
}