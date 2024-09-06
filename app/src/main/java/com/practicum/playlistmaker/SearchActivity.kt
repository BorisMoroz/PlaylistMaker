package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
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

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search() }

    private lateinit var inputEditText: EditText

    private lateinit var recyclerViewSearchResult : RecyclerView
    private lateinit var trackAdapter : TrackAdapter

    private lateinit var messagePlaceHolder : ImageView
    private lateinit var message : TextView
    private lateinit var buttonUpdate : Button

    private lateinit var searchHistoryLayOut : LinearLayout

    private lateinit var recyclerViewSearchHistory : RecyclerView
    private lateinit var trackAdapterSearchHistory : TrackAdapter

    private lateinit var searchHistoryTitle : TextView
    private lateinit var buttonClearHistory : Button

    private lateinit var searchProgressBar : ProgressBar

    private lateinit var inputMethod : InputMethodManager

    private lateinit var searchHistory : SearchHistory

    var tracks: ArrayList<Track> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBack = findViewById<ImageView>(R.id.button_back)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        inputEditText = findViewById<EditText>(R.id.inputEditText)

        searchHistory = SearchHistory((applicationContext as App).sharedPrefs)

        searchHistoryLayOut = findViewById<LinearLayout>(R.id.searchHistory)

        searchHistoryTitle = findViewById<TextView>(R.id.searchHistoryTitle)
        buttonClearHistory = findViewById<Button>(R.id.buttonClearHistory)

        recyclerViewSearchResult = findViewById<RecyclerView>(R.id.trackList)
        recyclerViewSearchResult.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        trackAdapter = TrackAdapter(tracks, onSearchResultChoosedTrack1)
        recyclerViewSearchResult.adapter = trackAdapter

        recyclerViewSearchHistory = findViewById<RecyclerView>(R.id.searchHistoryList)
        recyclerViewSearchHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        trackAdapterSearchHistory = TrackAdapter(searchHistory.tracks, onSearchHistoryChoosedTrack1)
        recyclerViewSearchHistory.adapter = trackAdapterSearchHistory

        messagePlaceHolder = findViewById<ImageView>(R.id.messagePlaceHolder)
        message = findViewById<TextView>(R.id.message)
        buttonUpdate =findViewById<Button>(R.id.button_update)

        searchProgressBar = findViewById<ProgressBar>(R.id.progressBar)

        inputMethod = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        buttonBack.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")

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

                searchDebounce()

                if (inputEditText.hasFocus() && s!!.isEmpty() && searchHistory.tracks.isNotEmpty()){
                    inputEditText.setShowSoftInputOnFocus(false)

                    inputMethod.hideSoftInputFromWindow(inputEditText.windowToken, 0)

                    tracks.clear()
                    trackAdapter.notifyDataSetChanged()

                    showSearchHistory()
                }
            }
        }

        inputEditText.addTextChangedListener(inputTextWatcher)

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && (view as EditText).text.isEmpty() && !searchHistory.tracks.isEmpty()){
                (view as EditText).setShowSoftInputOnFocus(false)

                showSearchHistory()
            }
        }

        inputEditText.setOnClickListener {

            if (inputEditText.hasFocus() && inputEditText.text.isEmpty() && !searchHistory.tracks.isEmpty()){
                hideSearchHistory()

                inputEditText.setShowSoftInputOnFocus(true)

                inputMethod.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        buttonClearHistory.setOnClickListener {
            hideSearchHistory()
            searchHistory.clear()

            inputEditText.setShowSoftInputOnFocus(true)
        }
    }

    val onSearchResultChoosedTrack1: () -> Unit = {

        searchHistory.addTrack(choosedTrack)

        val pleerIntent = Intent(this, AudiopleerActivity::class.java)
        startActivity(pleerIntent)
    }

    val onSearchHistoryChoosedTrack1: () -> Unit = {
        val pleerIntent = Intent(this, AudiopleerActivity::class.java)
        startActivity(pleerIntent)
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
        val query = inputEditText.text.toString()

        if (query.isNotEmpty()) {
            inputMethod.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            hideSearchResults()
            searchProgressBar.visibility = View.VISIBLE

            iTunesService.findSong(query).enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    searchProgressBar.visibility = View.INVISIBLE

                    tracks.clear()
                    if (response.code() == 200) {
                        if (response.body()?.resultCount != 0) tracks.addAll(response.body()?.results!!)

                        trackAdapter.notifyDataSetChanged()

                        if (tracks.isEmpty()) showMessage(SearchResult.NOTHING)
                        else {
                            showMessage(SearchResult.OK)
                            showSearchResults()
                        }
                    } else {
                        trackAdapter.notifyDataSetChanged()
                        showMessage(SearchResult.PROBLEM)
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    searchProgressBar.visibility = View.INVISIBLE

                    tracks.clear()
                    trackAdapter.notifyDataSetChanged()
                    showMessage(SearchResult.PROBLEM)
                }
            })
        }
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

    private fun showSearchHistory(){
        searchHistoryTitle.visibility = View.VISIBLE
        recyclerViewSearchHistory.visibility = View.VISIBLE
        buttonClearHistory.visibility = View.VISIBLE

        trackAdapterSearchHistory.notifyDataSetChanged()
    }

    private fun hideSearchHistory(){
        searchHistoryTitle.visibility = View.INVISIBLE
        recyclerViewSearchHistory.visibility = View.INVISIBLE
        buttonClearHistory.visibility = View.INVISIBLE
    }

    private fun showSearchResults(){
        recyclerViewSearchResult.visibility = View.VISIBLE
    }

    private fun hideSearchResults(){
        recyclerViewSearchResult.visibility = View.INVISIBLE
    }

    private fun hideMessage() {
        messagePlaceHolder.isVisible = false
        message.isVisible = false
        buttonUpdate.isVisible = false
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private enum class SearchResult {
        OK,
        NOTHING,
        PROBLEM
    }

    companion object {
        private const val INPUT_VALUE = "INPUT_VALUE"
        private const val INPUT_VALUE_DEF = ""

        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}