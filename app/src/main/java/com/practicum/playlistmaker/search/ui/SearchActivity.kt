package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.AudioPleerViewModel
import com.practicum.playlistmaker.player.ui.AudiopleerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    private var inputValue: String? = INPUT_VALUE_DEF

    private val handler = Handler(Looper.getMainLooper())

    private val viewModel by viewModel<SearchViewModel>()

    private val searchRunnable = Runnable {
        val query = inputEditText.text.toString()
        if (query.isNotEmpty()) {
            inputMethod.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            viewModel.search(query)
        }
    }

    private lateinit var inputEditText: EditText

    private lateinit var recyclerViewSearchResult: RecyclerView
    private lateinit var trackAdapter: TrackAdapter

    private lateinit var messagePlaceHolder: ImageView
    private lateinit var message: TextView
    private lateinit var buttonUpdate: Button

    private lateinit var searchHistoryLayOut: LinearLayout

    private lateinit var recyclerViewSearchHistory: RecyclerView
    private lateinit var trackAdapterSearchHistory: TrackAdapter

    private lateinit var searchHistoryTitle: TextView
    private lateinit var buttonClearHistory: Button

    private lateinit var searchProgressBar: ProgressBar

    private lateinit var inputMethod: InputMethodManager

    var tracks: ArrayList<Track> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel.getState().observe(this) { state ->
            renderState(state)
        }

        val buttonBack = findViewById<ImageView>(R.id.button_back)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        inputEditText = findViewById<EditText>(R.id.inputEditText)

        searchHistoryLayOut = findViewById<LinearLayout>(R.id.searchHistory)

        searchHistoryTitle = findViewById<TextView>(R.id.searchHistoryTitle)
        buttonClearHistory = findViewById<Button>(R.id.buttonClearHistory)

        recyclerViewSearchResult = findViewById<RecyclerView>(R.id.trackList)
        recyclerViewSearchResult.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        trackAdapter = TrackAdapter(tracks, onSearchResultChoosedTrack)
        recyclerViewSearchResult.adapter = trackAdapter

        recyclerViewSearchHistory = findViewById<RecyclerView>(R.id.searchHistoryList)
        recyclerViewSearchHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        trackAdapterSearchHistory =
            TrackAdapter(viewModel.getSearchHistoryTracks(), onSearchHistoryChoosedTrack)

        recyclerViewSearchHistory.adapter = trackAdapterSearchHistory

        messagePlaceHolder = findViewById<ImageView>(R.id.messagePlaceHolder)
        message = findViewById<TextView>(R.id.message)
        buttonUpdate = findViewById<Button>(R.id.button_update)

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

            val query = inputEditText.text.toString()
            viewModel.search(query)
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

                if (inputEditText.hasFocus() && s!!.isEmpty() && !viewModel.isSearchHistoryEmpty()) {
                    inputMethod.hideSoftInputFromWindow(inputEditText.windowToken, 0)

                    tracks.clear()
                    trackAdapter.notifyDataSetChanged()

                    inputEditText.setShowSoftInputOnFocus(false)

                    showSearchHistory()
                } else
                    inputEditText.setShowSoftInputOnFocus(true)

                searchDebounceNew()
            }
        }

        inputEditText.addTextChangedListener(inputTextWatcher)

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && (view as EditText).text.isEmpty() && !viewModel.isSearchHistoryEmpty()) {
                (view as EditText).setShowSoftInputOnFocus(false)

                showSearchHistory()
            } else
                (view as EditText).setShowSoftInputOnFocus(true)
        }

        inputEditText.setOnClickListener {
            hideMessage()

            if (inputEditText.hasFocus() && inputEditText.text.isEmpty() && !viewModel.isSearchHistoryEmpty()) {
                hideSearchHistory()
                inputEditText.setShowSoftInputOnFocus(true)
                inputMethod.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
            } else
                inputEditText.setShowSoftInputOnFocus(false)
        }

        buttonClearHistory.setOnClickListener {
            hideSearchHistory()
            viewModel.clearSearchHistory()

            inputEditText.setShowSoftInputOnFocus(true)
        }
    }

    val onSearchResultChoosedTrack: () -> Unit = {
        viewModel.addSearchHistoryTrack(choosedTrack)

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

    private fun showMessage(searchResult: SearchResult) {
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

    private fun showSearchHistory() {
        searchHistoryTitle.visibility = View.VISIBLE
        recyclerViewSearchHistory.visibility = View.VISIBLE
        buttonClearHistory.visibility = View.VISIBLE

        trackAdapterSearchHistory.notifyDataSetChanged()
    }

    private fun hideSearchHistory() {
        searchHistoryTitle.visibility = View.INVISIBLE
        recyclerViewSearchHistory.visibility = View.INVISIBLE
        buttonClearHistory.visibility = View.INVISIBLE
    }

    private fun showSearchResults() {
        recyclerViewSearchResult.visibility = View.VISIBLE
    }

    private fun hideSearchResults() {
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

    private fun renderState(state: SearchTracksState) {
        when (state) {
            is SearchTracksState.Error -> showError()
            is SearchTracksState.Content -> showTracks(state.data)
            is SearchTracksState.Loading -> showLoading()
        }
    }

    private fun showError() {
        searchProgressBar.visibility = View.INVISIBLE
        tracks.clear()
        trackAdapter.notifyDataSetChanged()
        showMessage(SearchResult.PROBLEM)
    }

    private fun showTracks(data: List<Track>) {
        searchProgressBar.visibility = View.INVISIBLE
        tracks.clear()
        tracks.addAll(data)
        trackAdapter.notifyDataSetChanged()

        if (tracks.isEmpty()) showMessage(SearchResult.NOTHING)
        else {
            showMessage(SearchResult.OK)
            showSearchResults()
        }
    }

    private fun showLoading() {
        hideSearchResults()
        searchProgressBar.visibility = View.VISIBLE
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
