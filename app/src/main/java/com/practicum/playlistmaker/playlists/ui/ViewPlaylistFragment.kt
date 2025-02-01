package com.practicum.playlistmaker.playlists.ui

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentViewPlaylistBinding
import com.practicum.playlistmaker.player.ui.choosedPlaylist
import com.practicum.playlistmaker.player.ui.getEnding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.search.ui.choosedTrack
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class ViewPlaylistFragment : Fragment() {
    private val viewModel by viewModel<ViewPlaylistViewModel>()

    private lateinit var binding: FragmentViewPlaylistBinding

    private lateinit var menubottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var recyclerViewPlaylistTracks: RecyclerView
    private lateinit var trackAdapter: TrackAdapter

    lateinit var deleteTrackDialog: MaterialAlertDialogBuilder
    lateinit var deletePlaylistDialog: MaterialAlertDialogBuilder

    var tracks: ArrayList<Track> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Glide.with(this)
            .load(choosedPlaylist!!.cover)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop())
            .into(binding.coverImage)

        binding.title.text = choosedPlaylist!!.title

        if (choosedPlaylist!!.description.isNotEmpty()) {
            binding.description.text = choosedPlaylist!!.description
        } else {
            binding.description.visibility = View.GONE
        }

        recyclerViewPlaylistTracks = binding.tracksList
        recyclerViewPlaylistTracks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        trackAdapter = TrackAdapter(
            tracks,
            onChoosedTrack,
            onChoosedTrackLong,
            viewLifecycleOwner.lifecycleScope
        )
        recyclerViewPlaylistTracks.adapter = trackAdapter

        val bottomSheet = binding.bottomSheetMenu
        menubottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        menubottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        menubottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        val tracksNumberPlural = this.getResources().getQuantityString(
            R.plurals.plurals_for_tracks,
            choosedPlaylist!!.tracksNum,
            choosedPlaylist!!.tracksNum
        )

        binding.playlistName.text = choosedPlaylist!!.title
        binding.playlistTracksNum.text = tracksNumberPlural

        val radiusInPixels = getResources()
            .getDimensionPixelSize(R.dimen.playlist_item_image_radius)

        Glide.with(this)
            .load(choosedPlaylist!!.cover)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(radiusInPixels))
            .into(binding.image)

        binding.backArrow.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.menu.setOnClickListener {
            menubottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.share.setOnClickListener {
            sharePlaylist()
        }

        binding.shareMenuItem.setOnClickListener {
            menubottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            sharePlaylist()
        }

        binding.removeMenuItem.setOnClickListener {
            menubottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            deletePlaylistDialog.show()
        }

        binding.editMenuItem.setOnClickListener {
            findNavController().navigate(R.id.action_viewPlaylistFragment_to_editPlaylistFragment)
        }

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        viewModel.getPlaylistTacks(choosedPlaylist)

        createDeleteTrackDialog()
        createDeletePlaylistDialog()
    }

    fun sharePlaylist() {
        if (choosedPlaylist!!.tracksNum == 0) {
            val toast_view = layoutInflater.inflate(R.layout.custom_toast, null)
            val toast_message = toast_view.findViewById<TextView>(R.id.text)

            toast_message.text = getString(R.string.share_menu_toast_phrase)

            val toast = Toast.makeText(requireActivity(), "", Toast.LENGTH_SHORT)
            toast.view = toast_view
            toast.show()
        } else {
            val tracksNumberPlural = this.getResources().getQuantityString(
                R.plurals.plurals_for_tracks,
                choosedPlaylist!!.tracksNum,
                choosedPlaylist!!.tracksNum
            )
            val messagePart1 =
                "${choosedPlaylist!!.title}\n${choosedPlaylist!!.description}\n${tracksNumberPlural}"
            var messagePart2 = ""

            for (i in tracks.indices) {
                messagePart2 += "\n" + (i + 1).toString() + ". " + tracks[i].artistName + " - " + tracks[i].trackName + "(" +
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(tracks[i].trackTimeMillis) + ")"
            }
            viewModel.sharePlaylist(messagePart1 + messagePart2)
        }
    }

    private fun createDeleteTrackDialog() {
        deleteTrackDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle("")
            .setMessage(getString(R.string.delete_track_dialog_message))
            .setNegativeButton(getString(R.string.dialog_negative_button)) { dialog, which ->
            }.setPositiveButton(getString(R.string.dialog_positive_button)) { dialog, which ->
                viewModel.removeTrackFromPlayList(choosedTrack, choosedPlaylist)
            }
    }

    private fun createDeletePlaylistDialog() {
        deletePlaylistDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle("")
            .setMessage("${getString(R.string.delete_playlist_dialog_message)} \"${choosedPlaylist!!.title}\"?")
            .setNegativeButton((getString(R.string.dialog_negative_button))) { dialog, which ->
            }.setPositiveButton(getString(R.string.dialog_positive_button)) { dialog, which ->
                viewModel.deletePlaylist(choosedPlaylist)
                choosedPlaylist = null
                clearTracks()
                findNavController().navigateUp()
            }
    }

    val onChoosedTrack: () -> Unit = {
        findNavController().navigate(R.id.action_viewPlaylistFragment_to_audioPleerFragment2)
    }

    val onChoosedTrackLong: () -> Unit = {
        deleteTrackDialog.show()
    }

    private fun renderState(state: PlaylistTracksState) {
        when (state) {
            is PlaylistTracksState.Content -> {
                var sumDuration: Long = 0

                for (track in state.data) {
                    sumDuration += track.trackTimeMillis
                }
                val sumDurationMinutes: Int = (sumDuration / (60 * 1000)).toInt()

                val sumDurationPlural = this.getResources().getQuantityString(
                    R.plurals.plurals_for_minutes,
                    sumDurationMinutes,
                    sumDurationMinutes
                )
                val tracksNumberPlural = this.getResources().getQuantityString(
                    R.plurals.plurals_for_tracks,
                    state.data.size,
                    state.data.size
                )

                binding.duration.text = sumDurationPlural + " • " + tracksNumberPlural
                showTracks(state.data)
            }

            is PlaylistTracksState.Empty -> {
                val sumDurationPlural =
                    this.getResources().getQuantityString(R.plurals.plurals_for_minutes, 0, 0)
                val tracksNumberPlural =
                    this.getResources().getQuantityString(R.plurals.plurals_for_tracks, 0, 0)
                binding.duration.text = sumDurationPlural + " • " + tracksNumberPlural
                clearTracks()
            }
        }
    }

    private fun showTracks(data: List<Track>) {
        tracks.clear()
        tracks.addAll(data)
        trackAdapter.notifyDataSetChanged()
    }

    private fun clearTracks() {
        tracks.clear()
        trackAdapter.notifyDataSetChanged()
    }
}