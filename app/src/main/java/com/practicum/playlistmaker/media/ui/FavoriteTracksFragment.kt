package com.practicum.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    private val viewModel by viewModel<FavoriteTracksViewModel>()

    private lateinit var binding: FragmentFavoriteTracksBinding
    private lateinit var recyclerViewFavoriteTracks: RecyclerView
    private lateinit var trackAdapter: TrackAdapter

    var tracks: ArrayList<Track> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewFavoriteTracks = binding.trackList
        recyclerViewFavoriteTracks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        trackAdapter = TrackAdapter(tracks, onChoosedTrack, viewLifecycleOwner.lifecycleScope)
        recyclerViewFavoriteTracks.adapter = trackAdapter

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getFavoriteTracks()
    }

    val onChoosedTrack: () -> Unit = {
        findNavController().navigate(R.id.action_mediaFragment_to_audiopleerActivity)
    }

    fun showNothingMessage(){
        binding.messagePlaceHolder.visibility = View.VISIBLE
        binding.message.visibility = View.VISIBLE
    }

    fun hideNothingMessage(){
        binding.messagePlaceHolder.visibility = View.GONE
        binding.message.visibility = View.GONE
    }

    private fun renderState(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.Content -> {

                hideNothingMessage()
                showTracks(state.data)

            }
            is FavoriteTracksState.Empty -> {
                hideTracks()
                showNothingMessage()
            }
        }
    }

    private fun showTracks(data: List<Track>) {
        tracks.clear()
        tracks.addAll(data)
        trackAdapter.notifyDataSetChanged()
    }

    private fun hideTracks() {
        tracks.clear()
        trackAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
        }
}