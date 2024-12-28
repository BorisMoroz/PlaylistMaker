package com.practicum.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.ui.choosedTrack
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class AudiopleerActivity : AppCompatActivity() {
    private lateinit var playButton: ImageButton
    private lateinit var favoriteButton: ImageButton
    private lateinit var trackDurationTextView: TextView

    private lateinit var playButtonState: PlayButtonState

    private val viewModel by viewModel<AudioPleerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audiopleer)

        viewModel.getAudioPlayerState().observe(this) { state ->
            renderState(state)
        }

        viewModel.getTrackFavoriteState().observe(this) { state ->
            renderFavoriteButtonState(state)
        }

        val backArrow = findViewById<ImageButton>(R.id.back_arrow)

        backArrow.setOnClickListener {
            finish()
        }

        val coverImage = findViewById<ImageView>(R.id.cover_image)
        val trackName = findViewById<TextView>(R.id.trackTextView)
        val artistName = findViewById<TextView>(R.id.artistTextView)
        val country = findViewById<TextView>(R.id.countryValueTextView)
        val genre = findViewById<TextView>(R.id.genreValueTextView)
        val year = findViewById<TextView>(R.id.yearValueTextView)
        val album = findViewById<TextView>(R.id.albumValueTextView)
        val duration = findViewById<TextView>(R.id.durationValueTextView)

        playButton = findViewById<ImageButton>(R.id.play_button)
        favoriteButton = findViewById<ImageButton>(R.id.favorite_button)
        trackDurationTextView = findViewById<TextView>(R.id.trackDurationTextView)

       val radiusInPixels = getResources().getDimensionPixelSize(R.dimen.cover_image_radius)

        Glide.with(this)
            .load(choosedTrack.getCoverArtwork())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.placeholder_pleer)
            .centerCrop()
            .transform(RoundedCorners(radiusInPixels))
            .into(coverImage)

        trackName.text = choosedTrack.trackName
        artistName.text = choosedTrack.artistName
        country.text = choosedTrack.country
        genre.text = choosedTrack.primaryGenreName
        year.text = choosedTrack.getReleaseYear()
        album.text = choosedTrack.collectionName
        duration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(choosedTrack.trackTimeMillis)

        playButton.isEnabled = false
        playButtonState = PlayButtonState.PLAY

        playButton.isEnabled = true

        playButton.setOnClickListener {
            when (playButtonState) {
                PlayButtonState.PLAY -> startPlayer()
                PlayButtonState.PAUSE -> pausePlayer()
            }
        }

        favoriteButton.setOnClickListener {
            changeTrackFavoriteState()
        }
    }

    private fun startPlayer() {
        playButton.setImageResource(R.drawable.ic_pause_button)
        playButtonState = PlayButtonState.PAUSE

        viewModel.startPlayer()
    }

    private fun pausePlayer() {
        playButton.setImageResource(R.drawable.ic_play_button)
        playButtonState = PlayButtonState.PLAY

        viewModel.pausePlayer()
    }

    private fun changeTrackFavoriteState(){
        viewModel.changeTrackFavoriteState()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.pausePlayer()
    }
    private fun renderState(state: AudioPlayerState) {
        when (state) {
            is AudioPlayerState.Completed -> {
                playButton.setImageResource(R.drawable.ic_play_button)
                playButtonState = PlayButtonState.PLAY
                trackDurationTextView.text = state.time
            }
            is AudioPlayerState.Default -> trackDurationTextView.text = state.time
            is AudioPlayerState.Prepared -> trackDurationTextView.text = state.time
            is AudioPlayerState.Playing -> trackDurationTextView.text = state.time
            is AudioPlayerState.Paused -> trackDurationTextView.text = state.time
        }
    }

    private fun renderFavoriteButtonState(state: Boolean){
        when (state){
            true -> favoriteButton.setImageResource(R.drawable.ic_favorite_active_button)
            false -> favoriteButton.setImageResource(R.drawable.ic_favorite_passive_button)
        }
    }

    private enum class PlayButtonState {
        PLAY,
        PAUSE
    }
}