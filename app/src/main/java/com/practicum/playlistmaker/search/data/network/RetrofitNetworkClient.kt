package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val iTunesApi: ITunesApi) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        try {
            if (dto is TracksSearchRequest) {
                return withContext(Dispatchers.IO) {
                    val resp = iTunesApi.findSong(dto.text)
                    resp.apply { resultCode = 200 }
                }
            } else {
                return Response().apply { resultCode = 400 }
            }
        }
        catch (ex : Exception){
            return Response().apply { resultCode = 400 }
        }
    }
}