package com.practicum.playlistmaker.presentation.ui.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.presentation.ui.player.AudiopleerActivity
import com.practicum.playlistmaker.R
//import com.practicum.playlistmaker.SearchHistory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.data.network.ITunesApi
import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.consumer.ConsumerData
import com.practicum.playlistmaker.domain.interactor.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.models.Track
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private var inputValue: String? = INPUT_VALUE_DEF

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { search() }

    private val SearchTracksUseCase = Creator.provideSearchTracksUseCase()

    private lateinit var searchHistoryInteractor : SearchHistoryInteractor

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

    var tracks: ArrayList<Track> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBack = findViewById<ImageView>(R.id.button_back)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        inputEditText = findViewById<EditText>(R.id.inputEditText)

        searchHistoryInteractor = Creator.provideSearchHistoryInteractor( /*sharedPrefs*/  (applicationContext as App).sharedPrefs)

        searchHistoryLayOut = findViewById<LinearLayout>(R.id.searchHistory)

        searchHistoryTitle = findViewById<TextView>(R.id.searchHistoryTitle)
        buttonClearHistory = findViewById<Button>(R.id.buttonClearHistory)

        recyclerViewSearchResult = findViewById<RecyclerView>(R.id.trackList)
        recyclerViewSearchResult.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        trackAdapter = TrackAdapter(tracks, onSearchResultChoosedTrack)
        recyclerViewSearchResult.adapter = trackAdapter

        recyclerViewSearchHistory = findViewById<RecyclerView>(R.id.searchHistoryList)
        recyclerViewSearchHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        trackAdapterSearchHistory = TrackAdapter(searchHistoryInteractor.getSearchHistoryTracks(), onSearchHistoryChoosedTrack)
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
                if (s.isNullOrEmpty()) hideSearchHistory()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                inputValue = s?.toString()
                hideMessage()

                if (inputEditText.hasFocus() && s!!.isEmpty() && !searchHistoryInteractor/*searchHistory.tracks*/.isEmpty()){
                    inputMethod.hideSoftInputFromWindow(inputEditText.windowToken, 0)

                    tracks.clear()
                    trackAdapter.notifyDataSetChanged()

                    inputEditText.setShowSoftInputOnFocus(false)

                    showSearchHistory()
                }
                else
                    inputEditText.setShowSoftInputOnFocus(true)

                searchDebounceNew()
            }
        }

        inputEditText.addTextChangedListener(inputTextWatcher)

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && (view as EditText).text.isEmpty() && !searchHistoryInteractor/*searchHistory.tracks*/.isEmpty()){
                (view as EditText).setShowSoftInputOnFocus(false)

                showSearchHistory()
            }
            else
                (view as EditText).setShowSoftInputOnFocus(true)
        }

        inputEditText.setOnClickListener {
            hideMessage()

            if (inputEditText.hasFocus() && inputEditText.text.isEmpty() && !searchHistoryInteractor/*searchHistory.tracks*/.isEmpty()){
                hideSearchHistory()
                inputEditText.setShowSoftInputOnFocus(true)
                inputMethod.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
            }
            else
                inputEditText.setShowSoftInputOnFocus(false)
        }

        buttonClearHistory.setOnClickListener {
            hideSearchHistory()
            searchHistoryInteractor/*searchHistory*/.clear()

            inputEditText.setShowSoftInputOnFocus(true)
        }
    }

    val onSearchResultChoosedTrack: () -> Unit = {

        //searchHistory.addTrack(choosedTrack)

        searchHistoryInteractor/*searchHistory*/.addTrack(choosedTrack)

        val pleerIntent = Intent(this, AudiopleerActivity::class.java)
        startActivity(pleerIntent)
    }

    val onSearchHistoryChoosedTrack: () -> Unit = {
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

        val consumer = object : Consumer<List<Track>> {
            override fun consume(data: ConsumerData<List<Track>>) {
                val newRunnable = Runnable {
                    when (data) {
                        is ConsumerData.Error -> {
                            searchProgressBar.visibility = View.INVISIBLE
                            tracks.clear()
                            trackAdapter.notifyDataSetChanged()
                            showMessage(SearchResult.PROBLEM)
                        }
                        is ConsumerData.Data -> {
                            searchProgressBar.visibility = View.INVISIBLE
                            tracks.clear()
                            tracks.addAll(data.value)
                            trackAdapter.notifyDataSetChanged()

                            if (tracks.isEmpty()) showMessage(SearchResult.NOTHING)
                            else {
                                showMessage(SearchResult.OK)
                                showSearchResults()
                            }
                        }
                    }
                }
                handler.post(newRunnable)
            }
        }

        if (query.isNotEmpty()) {
            inputMethod.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            hideSearchResults()
            searchProgressBar.visibility = View.VISIBLE

            SearchTracksUseCase.execute(query,consumer)
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
    private fun searchDebounceNew() {
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
