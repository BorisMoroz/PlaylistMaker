package com.practicum.playlistmaker.media.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayListsBinding
import com.practicum.playlistmaker.player.ui.choosedPlaylist
import com.practicum.playlistmaker.playlists.domain.models.Playlist
import com.practicum.playlistmaker.search.ui.choosedTrack
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment : Fragment() {
    private val viewModel by viewModel<PlayListsViewModel>()

    private lateinit var binding: FragmentPlayListsBinding
    private lateinit var recyclerViewPlaylists: RecyclerView
    private lateinit var playlistAdapter: PlaylistAdapter

    var playlists: ArrayList<Playlist> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPlayListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewPlaylists = binding.playlistsList
        recyclerViewPlaylists.layoutManager =  GridLayoutManager(requireContext(), 2)

        playlistAdapter = PlaylistAdapter(playlists, onChoosedPlaylist)
        recyclerViewPlaylists.adapter = playlistAdapter

        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment)
        }

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            renderState(state)}
    }

    override fun onResume() {
        super.onResume()

        viewModel.getPlaylists()
    }

    val onChoosedPlaylist: () -> Unit = {
        findNavController().navigate(R.id.action_mediaFragment_to_viewPlaylistFragment)
    }

    fun showNothingMessage(){
        binding.messagePlaceHolder.visibility = View.VISIBLE
        binding.message.visibility = View.VISIBLE
       }

    fun hideNothingMessage(){
        binding.messagePlaceHolder.visibility = View.GONE
        binding.message.visibility = View.GONE
    }

    private fun showPlaylists(data: List<Playlist>) {
        playlists.clear()
        playlists.addAll(data)
        playlistAdapter.notifyDataSetChanged()
    }

    private fun hidePlaylists() {
        playlists.clear()
        playlistAdapter.notifyDataSetChanged()
    }

    private fun renderState(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Content -> {
                hideNothingMessage()
                showPlaylists(state.data)
            }
            is PlaylistsState.Empty -> {
                hidePlaylists()
                showNothingMessage()
            }
        }
    }

    companion object {
        fun newInstance() = PlayListsFragment()
    }
}
