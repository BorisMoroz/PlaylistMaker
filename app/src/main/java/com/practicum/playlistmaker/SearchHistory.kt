package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val searchHistoryLength = 10

class SearchHistory (val sharedPrefs : SharedPreferences) {

    var tracks : ArrayList<Track> = arrayListOf()

    init {
        loadTracks()
    }

    fun loadTracks() {
        val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, null)
        if (json != null) {
            val typeToken = object : TypeToken<ArrayList<Track>>() {}.type
            tracks = Gson().fromJson(json, typeToken)
        }
    }

    fun saveTracks(){
        val json = Gson().toJson(tracks)

        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    fun addTrack(newTrack : Track){

        var trackFound = false
        var trackIndex = 0

        for (i in tracks.indices) {
            if (tracks[i].trackId == newTrack.trackId) {
                trackFound = true
                trackIndex = i
                break
            }
        }

        if (trackFound)
            tracks.removeAt(trackIndex)

        tracks.add(0, newTrack)

        if (tracks.size > searchHistoryLength)
            tracks.removeAt(tracks.size-1)

        saveTracks()
    }

    fun clear(){
        tracks.clear()
        saveTracks()
    }
}