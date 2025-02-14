package com.practicum.playlistmaker.playlists.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.choosedPlaylist
import com.practicum.playlistmaker.playlists.domain.models.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : CreatePlaylistFragment() {
    override val viewModel by viewModel<EditPlaylistViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleTextView.text = getString(R.string.edit_playlist_title)
        binding.buttonCreatePlaylist.text = getString(R.string.edit_playlist_button)
        binding.playlistNameEditText.setText(choosedPlaylist!!.title)
        binding.playlistDescriptionEditText.setText(choosedPlaylist!!.description)

        val radiusInPixels =
            getResources().getDimensionPixelSize(R.dimen.playlist_cover_image_radius)

        Glide.with(this)
            .load(choosedPlaylist!!.cover)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .transform(
                CenterCrop(),
                RoundedCorners(radiusInPixels),
            )
            .into(binding.playlistImage)

        backPressedCallback.isEnabled = false

        binding.buttonCreatePlaylist.setOnClickListener {
            if (playlistImageUri != null || binding.playlistNameEditText.getText()
                    .toString() != choosedPlaylist!!.title
                || binding.playlistDescriptionEditText.getText()
                    .toString() != choosedPlaylist!!.description
            ) {
                viewModel.saveImageToPrivateStorage(playlistImageUri)
            }
            findNavController().navigateUp()
        }
    }

    override fun rendersaveImageToPrivateStorage(state: String){
        val playlistImageAbsolutePath : String

        if (state.isEmpty()) {
            playlistImageAbsolutePath = choosedPlaylist!!.cover
        }
        else{
            playlistImageAbsolutePath = state
        }

        val playlist = Playlist(
            choosedPlaylist!!.id,
            binding.playlistNameEditText.getText().toString(),
            binding.playlistDescriptionEditText.getText().toString(),
            playlistImageAbsolutePath,
            choosedPlaylist!!.tracksIds, choosedPlaylist!!.tracksNum
        )

        viewModel.updatePlaylist(playlist)
        choosedPlaylist = playlist

        findNavController().navigateUp()
    }
}
