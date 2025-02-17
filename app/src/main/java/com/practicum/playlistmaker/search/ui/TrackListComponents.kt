package com.practicum.playlistmaker.search.ui

import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

lateinit var choosedTrack : Track

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val image = itemView.findViewById<ImageView>(R.id.image)

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName + " • " + SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        val radiusInPixels = itemView.context.getResources()
            .getDimensionPixelSize(R.dimen.tracklst_item_image_radius)

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(radiusInPixels))
            .into(image)
    }
}
class TrackAdapter(private val tracks: List<Track>, val onChoosedTrack : () -> Unit, val onChoosedTrackLong : () -> Unit, val scope : CoroutineScope) : RecyclerView.Adapter<TrackViewHolder>() {
    private var choosetrackJob: Job? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            choosedTrack = tracks[position]
            chooseTrackDebounce()
        }

        holder.itemView.setOnLongClickListener {
            choosedTrack = tracks[position]

            onChoosedTrackLong.invoke()

            return@setOnLongClickListener true
        }
    }
    override fun getItemCount(): Int {
        return tracks.size
    }
    private fun chooseTrack(){
        onChoosedTrack.invoke()
    }

    private fun chooseTrackDebounce() {
        choosetrackJob?.cancel()

        choosetrackJob = scope.launch {
            delay(CHOOSE_TRACK_DEBOUNCE_DELAY)
            chooseTrack()
        }
    }
    companion object {
        private const val CHOOSE_TRACK_DEBOUNCE_DELAY = 500L
    }
}
