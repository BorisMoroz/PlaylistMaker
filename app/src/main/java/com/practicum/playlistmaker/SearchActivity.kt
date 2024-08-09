package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    private var inputValue: String? = INPUT_VALUE_DEF

    private lateinit var inputEditText: EditText
    private lateinit var trackAdapter : TrackAdapter

    private lateinit var messagePlaceHolder : ImageView
    private lateinit var message : TextView
    private lateinit var buttonUpdate : Button

    var tracks: ArrayList<Track> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBack = findViewById<ImageView>(R.id.button_back)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        inputEditText = findViewById<EditText>(R.id.inputEditText)

        val recyclerView = findViewById<RecyclerView>(R.id.trackList)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        trackAdapter = TrackAdapter(tracks)
        recyclerView.adapter = trackAdapter

        messagePlaceHolder = findViewById<ImageView>(R.id.messagePlaceHolder)
        message = findViewById<TextView>(R.id.message)
        buttonUpdate =findViewById<Button>(R.id.button_update)

        buttonBack.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")

            val inputMethod =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethod.hideSoftInputFromWindow(inputEditText.windowToken, 0)

            tracks.clear()

            trackAdapter.notifyDataSetChanged()
        }

        buttonUpdate.setOnClickListener {
            hideMessage()
            search()
        }

        val inputTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                inputValue = s?.toString()
                hideMessage()
            }
        }
        inputEditText.addTextChangedListener(inputTextWatcher)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) search()
            }
            false
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return s.isNullOrEmpty()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_VALUE, inputValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        inputValue = savedInstanceState.getString(INPUT_VALUE, INPUT_VALUE_DEF)
        inputEditText.setText(inputValue)
    }

    private fun search() {
        iTunesService.findSong(inputEditText.text.toString()).enqueue(object : Callback<TracksResponse> {
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>
            ) {
                tracks.clear()
                if (response.code() == 200) {

                    if (response.body()?.resultCount != 0) tracks.addAll(response.body()?.results!!)

                    trackAdapter.notifyDataSetChanged()

                    if (tracks.isEmpty()) showMessage(SearchResult.NOTHING)
                    else showMessage(SearchResult.OK)
                } else {
                    trackAdapter.notifyDataSetChanged()
                    showMessage(SearchResult.PROBLEM)
                }
            }
            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                tracks.clear()
                trackAdapter.notifyDataSetChanged()
                showMessage(SearchResult.PROBLEM)
            }
        })
    }
    private fun showMessage(searchResult : SearchResult) {
        when (searchResult) {
            SearchResult.OK -> {}
            SearchResult.NOTHING -> {
                messagePlaceHolder.setImageResource(R.drawable.placeholder_nothing)
                message.text = getString(R.string.search_screen_nothing_message)
                messagePlaceHolder.isVisible = true
                message.isVisible = true
            }
            SearchResult.PROBLEM -> {
                messagePlaceHolder.setImageResource(R.drawable.placeholder_problem)
                message.text = getString(R.string.search_screen_problem_message)
                messagePlaceHolder.isVisible = true
                message.isVisible = true
                buttonUpdate.isVisible = true
            }
        }
    }

    private fun hideMessage() {
        messagePlaceHolder.isVisible = false
        message.isVisible = false
        buttonUpdate.isVisible = false
    }

    private enum class SearchResult {
        OK,
        NOTHING,
        PROBLEM
    }

    companion object {
        private const val INPUT_VALUE = "INPUT_VALUE"
        private const val INPUT_VALUE_DEF = ""
    }
}

