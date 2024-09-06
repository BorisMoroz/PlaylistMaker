package com.practicum.playlistmaker

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.Locale

class AudiopleerActivity : AppCompatActivity() {
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
        val trackDuration = findViewById<TextView>(R.id.trackDurationTextView)
        val country = findViewById<TextView>(R.id.countryValueTextView)
        val genre = findViewById<TextView>(R.id.genreValueTextView)
        val year = findViewById<TextView>(R.id.yearValueTextView)
        val album = findViewById<TextView>(R.id.albumValueTextView)
        val duration = findViewById<TextView>(R.id.durationValueTextView)

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
    }
}