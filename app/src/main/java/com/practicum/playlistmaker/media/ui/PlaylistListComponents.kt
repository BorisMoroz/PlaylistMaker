package com.practicum.playlistmaker.media.ui

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlists.domain.models.Playlist

class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
    private val playlistTracksNum: TextView = itemView.findViewById(R.id.playlistTracksNum)
    private val image = itemView.findViewById<ImageView>(R.id.image)

    fun bind(playlist: Playlist) {
        playlistName.text = playlist.title

        val root = itemView.context.getResources().getString(R.string.playlist_tracks_number_root)
        val ending = getEnding(playlist.tracksNum, itemView.context)

        playlistTracksNum.text = "${playlist.tracksNum} " + root + ending

        val radiusInPixels = itemView.context.getResources()
                .getDimensionPixelSize(R.dimen.playlist_item_image_radius)

        Glide.with(itemView)
            .load(playlist.cover)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(),RoundedCorners(radiusInPixels))
            .into(image)
    }
}

class PlaylistAdapter(private val playlists: List<Playlist>) : RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_view, parent, false)
        return PlaylistViewHolder(view)
    }
    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
    override fun getItemCount(): Int {
        return playlists.size
    }
}

fun getEnding(number : Int, context: Context) : String{
    var end = ""

    when (number % 10) {
        0, in 5..9-> end = context.getResources().getString(R.string.playlist_tracks_number_ending_2)
        1 -> end = ""
        in 2..4 -> end = context.getResources().getString(R.string.playlist_tracks_number_ending_1)
    }
    return end
}
