package com.practicum.playlistmaker.media.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    private val viewModel by viewModel<FavoriteTracksFragmentViewModel>()

    private lateinit var binding: FragmentFavoriteTracksBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showNothingMessage()
    }

    fun showNothingMessage(){
        binding.messagePlaceHolder.visibility = View.VISIBLE
        binding.message.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
        }
}