package com.practicum.playlistmaker.presentation.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.Button
import com.practicum.playlistmaker.presentation.ui.media.MediaActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.ui.search.SearchActivity
import com.practicum.playlistmaker.presentation.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)

        val buttonSearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
               val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }
        buttonSearch.setOnClickListener(buttonSearchClickListener)

        val buttonMedia = findViewById<Button>(R.id.button_media)

        buttonMedia.setOnClickListener {
        val mediaIntent = Intent(this, MediaActivity::class.java)
        startActivity(mediaIntent)
        }

        val buttonSettings = findViewById<Button>(R.id.button_settings)

        buttonSettings.setOnClickListener {
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsIntent)
        }
    }
}