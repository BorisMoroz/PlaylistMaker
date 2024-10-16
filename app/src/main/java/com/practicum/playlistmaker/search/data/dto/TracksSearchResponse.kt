package com.practicum.playlistmaker.search.data.dto

class TracksSearchResponse(val resultCount : Int, val results : ArrayList<TrackDto>) : Response()