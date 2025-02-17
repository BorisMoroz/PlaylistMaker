package com.practicum.playlistmaker.playlists.ui

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylitstBinding
import com.practicum.playlistmaker.playlists.domain.models.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

open class CreatePlaylistFragment : Fragment() {
    open val viewModel by viewModel<CreatePlaylistViewModel>()

    lateinit var binding: FragmentCreatePlaylitstBinding

    var playlistImageUri: Uri? = null

    lateinit var backPressedDialog: MaterialAlertDialogBuilder

    val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFullContentNotEmpty()) {
                backPressedDialog.show()
            } else {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
                isEnabled = true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePlaylitstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getplaylistImageAbsolutePath().observe(viewLifecycleOwner) { state ->
            rendersaveImageToPrivateStorage(state)
        }

        binding.backArrow.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.buttonCreatePlaylist.setOnClickListener {
            viewModel.saveImageToPrivateStorage(playlistImageUri)
        }

        val inputNameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isEmpty()) {
                    binding.buttonCreatePlaylist.isEnabled = false
                } else {
                    binding.buttonCreatePlaylist.isEnabled = true
                }
            }
        }

        binding.playlistNameEditText.addTextChangedListener(inputNameTextWatcher)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                playlistImageUri = uri

                if (playlistImageUri != null) {

                    val radiusInPixels =
                        getResources().getDimensionPixelSize(R.dimen.playlist_cover_image_radius)

                    Glide.with(this)
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .transform(
                            CenterCrop(),
                            RoundedCorners(radiusInPixels),
                        )
                        .into(binding.playlistImage)
                } else {

                }
            }

        binding.playlistImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
        createBackPressedDialog()
    }

    private fun isFullContentNotEmpty(): Boolean {
        return binding.playlistImage.getDrawable() != null || binding.playlistNameEditText.text.isNotEmpty() || binding.playlistDescriptionEditText.text.isNotEmpty()
    }

    private fun createBackPressedDialog() {
        backPressedDialog =
            MaterialAlertDialogBuilder(requireActivity(), R.style.MyAlertDialogTheme)
                .setTitle(getString(R.string.create_playlist_backpressed_dialog_title))
                .setMessage(getString(R.string.create_playlist_backpressed_dialog_message))
                .setNeutralButton(getString(R.string.create_playlist_backpressed_dialog_cancel_button)) { dialog, which ->
                }
                .setPositiveButton(getString(R.string.create_playlist_backpressed_dialog_yes_button)) { dialog, which ->
                    findNavController().navigateUp()
                }
    }

    open fun rendersaveImageToPrivateStorage(state: String) {
        val playlist = Playlist(
            0,
            binding.playlistNameEditText.getText().toString(),
            binding.playlistDescriptionEditText.getText().toString(),
            state,
            mutableListOf(), 0
        )

        viewModel.addPlaylist(playlist)

        findNavController().navigateUp()

        val toast_view = layoutInflater.inflate(R.layout.custom_toast, null)
        val toast_message = toast_view.findViewById<TextView>(R.id.text)
        toast_message.text =
            "${getString(R.string.create_playlist_toast_phrase_first_part)} [${binding.playlistNameEditText.text}] ${getString(R.string.create_playlist_toast_phrase_second_part)}"
        val toast = Toast.makeText(requireActivity(), "", Toast.LENGTH_SHORT)
        toast.view = toast_view
        toast.show()
    }
}