package com.practicum.playlistmaker.media.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practicum.playlistmaker.databinding.FragmentPlayListsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment : Fragment() {
    private val viewModel by viewModel<PlayListsViewModel>()

    private lateinit var binding: FragmentPlayListsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPlayListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showNothingMessage()
    }

    fun showNothingMessage(){
        binding.buttonNewPlaylist.visibility = View.VISIBLE
        binding.messagePlaceHolder.visibility = View.VISIBLE
        binding.message.visibility = View.VISIBLE
       }

    companion object {
        fun newInstance() = PlayListsFragment()
    }
}