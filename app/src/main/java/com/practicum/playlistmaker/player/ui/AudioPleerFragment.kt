package com.practicum.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAudiopleerBinding
import com.practicum.playlistmaker.media.ui.PlaylistsState
import com.practicum.playlistmaker.playlists.domain.models.Playlist
import com.practicum.playlistmaker.playlists.ui.CreatePlaylistFragment
import com.practicum.playlistmaker.search.ui.choosedTrack
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class AudioPleerFragment : Fragment() {
    private lateinit var binding: FragmentAudiopleerBinding

    private lateinit var playButton: ImageButton
    private lateinit var favoriteButton: ImageButton
    private lateinit var addButton: ImageButton
    private lateinit var buttonNewPlaylist: Button

    private lateinit var trackDurationTextView: TextView

    private lateinit var playButtonState: AudioPleerFragment.PlayButtonState

    private lateinit var recyclerViewPlaylists: RecyclerView
    private lateinit var playlistAdapter: playerPlaylistAdapter

    private lateinit var  bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    var playlists: ArrayList<Playlist> = arrayListOf()

    private val viewModel by viewModel<AudioPleerViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAudiopleerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAudioPlayerState().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }
        viewModel.getTrackFavoriteState().observe(viewLifecycleOwner) { state ->
            renderFavoriteButtonState(state)
        }
        viewModel.getPlaylistsState().observe(viewLifecycleOwner) { state ->
            renderPlaylistsListState(state)}
        viewModel.getTrackInPlaylistState().observe(viewLifecycleOwner) { state ->
            renderTrackInPlaylistState(state)
        }

        val backArrow = binding.backArrow

        backArrow.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val coverImage = binding.coverImage
        val trackName = binding.trackTextView
        val artistName = binding.artistTextView
        val country = binding.countryValueTextView
        val genre = binding.genreValueTextView
        val year = binding.yearValueTextView
        val album = binding.albumValueTextView
        val duration = binding.durationValueTextView

        val bottomSheet = binding.bottomSheet

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        val overlay = binding.overlay

        playButton = binding.playButton
        favoriteButton = binding.favoriteButton
        addButton = binding.addButton

        buttonNewPlaylist = binding.buttonNewPlaylist

        trackDurationTextView = binding.trackDurationTextView

        recyclerViewPlaylists = binding.playlistsList

        val radiusInPixels = getResources().getDimensionPixelSize(R.dimen.cover_image_radius)

        Glide.with(this)
            .load(choosedTrack.getCoverArtwork())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.placeholder_pleer)
            .transform(CenterCrop(), RoundedCorners(radiusInPixels))
            .into(coverImage)

        trackName.text = choosedTrack.trackName
        artistName.text = choosedTrack.artistName
        country.text = choosedTrack.country
        genre.text = choosedTrack.primaryGenreName
        year.text = choosedTrack.getReleaseYear()
        album.text = choosedTrack.collectionName
        duration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(choosedTrack.trackTimeMillis)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        playButton.isEnabled = false
        playButtonState = AudioPleerFragment.PlayButtonState.PLAY

        playButton.isEnabled = true

        playButton.setOnClickListener {
            when (playButtonState) {
                AudioPleerFragment.PlayButtonState.PLAY -> startPlayer()
                AudioPleerFragment.PlayButtonState.PAUSE -> pausePlayer()
            }
        }

        favoriteButton.setOnClickListener {
            changeTrackFavoriteState()
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }
                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        addButton.setOnClickListener {
            viewModel.getPlaylists()

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        buttonNewPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            findNavController().navigate(R.id.action_audioPleerFragment2_to_createPlaylistFragment)
        }

        val onChoosedPlaylist: () -> Unit = {
            viewModel.addTrackToPlaylist(choosedTrack, choosedPlaylist)
        }

        recyclerViewPlaylists.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        playlistAdapter = playerPlaylistAdapter(playlists, onChoosedPlaylist)
        recyclerViewPlaylists.adapter = playlistAdapter
    }

    private fun startPlayer() {
        playButton.setImageResource(R.drawable.ic_pause_button)
        playButtonState = AudioPleerFragment.PlayButtonState.PAUSE

        viewModel.startPlayer()
    }

    private fun pausePlayer() {
        playButton.setImageResource(R.drawable.ic_play_button)
        playButtonState = AudioPleerFragment.PlayButtonState.PLAY

        viewModel.pausePlayer()
    }

    private fun changeTrackFavoriteState(){
        viewModel.changeTrackFavoriteState()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.pausePlayer()
    }

    private fun renderState(state: AudioPlayerState) {
        when (state) {
            is AudioPlayerState.Completed -> {
                playButton.setImageResource(R.drawable.ic_play_button)
                playButtonState = AudioPleerFragment.PlayButtonState.PLAY
                trackDurationTextView.text = state.time
            }
            is AudioPlayerState.Default -> trackDurationTextView.text = state.time
            is AudioPlayerState.Prepared -> trackDurationTextView.text = state.time
            is AudioPlayerState.Playing -> trackDurationTextView.text = state.time
            is AudioPlayerState.Paused -> trackDurationTextView.text = state.time
        }
    }

    private fun renderFavoriteButtonState(state: Boolean){
        when (state){
            true -> favoriteButton.setImageResource(R.drawable.ic_favorite_active_button)
            false -> favoriteButton.setImageResource(R.drawable.ic_favorite_passive_button)
        }
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

    private fun renderPlaylistsListState(state: PlaylistsState){

        when (state) {
            is PlaylistsState.Content -> {
                showPlaylists(state.data)
            }
            is PlaylistsState.Empty -> {
                hidePlaylists()
            }
        }
    }

    private fun renderTrackInPlaylistState(state: Boolean){
        val toast_view = layoutInflater.inflate(R.layout.custom_toast, null)
        val toast_message = toast_view.findViewById<TextView>(R.id.text)
        val toast = Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT)

        when (state){
            true -> {
                toast_message.text = "${getString(R.string.add_track_to_playlist_phrase_1)} [${choosedPlaylist.title}]"
            }
            false -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

                toast_message.text = "${getString(R.string.add_track_to_playlist_phrase_2)} [${choosedPlaylist.title}]"
            }
        }

        toast.view = toast_view
        toast.show()
    }

    private enum class PlayButtonState {
        PLAY,
        PAUSE
    }
}