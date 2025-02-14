package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var inputValue: String? = INPUT_VALUE_DEF

    private var searchJob: Job? = null

    private val viewModel by viewModel<SearchViewModel>()

    private lateinit var binding: FragmentSearchBinding

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
    var searchHistoryTracks: ArrayList<Track> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            inputValue = savedInstanceState.getString(INPUT_VALUE, INPUT_VALUE_DEF)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        viewModel.updateTracksFavotiteStatus()

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        val clearButton = binding.clearIcon
        inputEditText = binding.inputEditText

        inputEditText.setText(inputValue)

        searchHistoryLayOut = binding.searchHistory

        searchHistoryTitle = binding.searchHistoryTitle
        buttonClearHistory = binding.buttonClearHistory

        recyclerViewSearchResult = binding.trackList
        recyclerViewSearchResult.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        trackAdapter = TrackAdapter(tracks, onSearchResultChoosedTrack, onChoosedTrackLong, viewLifecycleOwner.lifecycleScope)
        recyclerViewSearchResult.adapter = trackAdapter

        recyclerViewSearchHistory = binding.searchHistoryList
        recyclerViewSearchHistory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        trackAdapterSearchHistory =
            TrackAdapter(searchHistoryTracks, onSearchHistoryChoosedTrack, onChoosedTrackLong, viewLifecycleOwner.lifecycleScope)

        recyclerViewSearchHistory.adapter = trackAdapterSearchHistory

        messagePlaceHolder = binding.messagePlaceHolder
        message = binding.message
        buttonUpdate = binding.buttonUpdate

        searchProgressBar = binding.progressBar

        inputMethod = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        clearButton.setOnClickListener {
            inputMethod.hideSoftInputFromWindow(inputEditText.windowToken, 0)

            tracks.clear()

            trackAdapter.notifyDataSetChanged()

            resetLastQuery()

            inputEditText.requestFocus()

            inputEditText.setText("")
        }

        buttonUpdate.setOnClickListener {
            hideMessage()
            resetLastQuery()
            searchRequest()
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

                    updateSearchHistoryTracks(viewModel.getSearchHistoryTracks())
                    showSearchHistory()

                    viewModel.resetSearchResults()
                } else{
                    inputEditText.setShowSoftInputOnFocus(true)}

                if(!s!!.isEmpty() && inputEditText.hasFocus()){
                    searchDebounceNew()}
            }
        }

        inputEditText.addTextChangedListener(inputTextWatcher)

        inputEditText.setOnFocusChangeListener { view, hasFocus ->

            if (hasFocus && (view as EditText).text.isEmpty() && !viewModel.isSearchHistoryEmpty()) {
                (view as EditText).setShowSoftInputOnFocus(false)
                updateSearchHistoryTracks(viewModel.getSearchHistoryTracks())
                showSearchHistory()

                viewModel.resetSearchResults()
            } else{
                }
        }

        inputEditText.setOnClickListener {
            hideMessage()

            if (inputEditText.hasFocus() && inputEditText.text.isEmpty() && !viewModel.isSearchHistoryEmpty()) {
                hideSearchHistory()
                inputMethod.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
            }
            else{
                }
        }

        buttonClearHistory.setOnClickListener {
            hideSearchHistory()
            viewModel.clearSearchHistory()

            inputEditText.setShowSoftInputOnFocus(true)
        }
    }

    val onSearchResultChoosedTrack: () -> Unit = {
        viewModel.addSearchHistoryTrack(choosedTrack)
        findNavController().navigate(R.id.action_searchFragment_to_audioPleerFragment2)
    }

    val onSearchHistoryChoosedTrack: () -> Unit = {
        findNavController().navigate(R.id.action_searchFragment_to_audioPleerFragment2)
    }

    val onChoosedTrackLong: () -> Unit = {
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return s.isNullOrEmpty()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_VALUE, inputValue)
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

    private fun updateSearchHistoryTracks(tracks: List<Track>){
        searchHistoryTracks.clear()
        searchHistoryTracks.addAll(tracks)
        trackAdapterSearchHistory.notifyDataSetChanged()
    }

    private fun showSearchHistory() {
        searchHistoryTitle.visibility = View.VISIBLE
        recyclerViewSearchHistory.visibility = View.VISIBLE
        buttonClearHistory.visibility = View.VISIBLE
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

    private fun resetLastQuery(){
        viewModel.resetLastQuery()
    }

    private fun searchRequest() {
        val query = inputEditText.text.toString()
        if (query.isNotEmpty()) {
            inputMethod.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            viewModel.search(query)
        }
    }

    private fun searchDebounceNew() {
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest()
        }
    }

    private fun renderState(state: SearchTracksState?) {
        when (state) {
            is SearchTracksState.Error -> showError()
            is SearchTracksState.Content -> showTracks(state.data)
            is SearchTracksState.Loading -> showLoading()
            else -> {
                hideSearchResults()
                tracks.clear()
            }
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
