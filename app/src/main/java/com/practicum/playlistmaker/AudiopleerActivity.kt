package com.practicum.playlistmaker

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.Locale

class AudiopleerActivity : AppCompatActivity() {
    private lateinit var playButton : ImageButton
    private lateinit var trackDurationTextView : TextView

    private lateinit var mediaPlayer : MediaPlayer

    private var playerState = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audiopleer)

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
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(choosedTrack.trackTimeMillis)

        playButton.isEnabled = false

        mediaPlayer = MediaPlayer()

        preparePlayer()

        playButton.setOnClickListener {
            playbackControl()
        }
    }
    override fun onPause() {
        super.onPause()
        if (playerState == STATE_PLAYING)
            pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        setTimerOff()
        mediaPlayer.release()
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(choosedTrack.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.ic_play_button)
            timerReset()
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.ic_pause_button)
        setTimerOn()
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.ic_play_button)
        setTimerOff()
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun setTimerOn(){
        handler.post(timerRunnable)
    }

    private fun setTimerOff() {
        handler.removeCallbacks(timerRunnable)
    }

    private fun timerReset(){
        setTimerOff()
        trackDurationTextView.text = getResources().getString(R.string.track_duration_check_value)
    }

    private val timerRunnable = object :  Runnable {
        override fun run() {
            trackDurationTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            handler.postDelayed(this, TIMER_STEP)
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val TIMER_STEP = 500L
    }
}