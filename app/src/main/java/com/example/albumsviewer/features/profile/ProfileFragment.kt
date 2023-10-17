package com.example.albumsviewer.features.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.albumsviewer.databinding.ProfileFragmentBinding
import com.example.albumsviewer.result.UiState
import com.example.albumsviewer.utils.collectAsStateWithLifecycle
import com.example.albumsviewer.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private val albumsAdapter: AlbumsAdapter by lazy {
        AlbumsAdapter { id ->
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToPhotosFragment(
                    id
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectState()
    }

    private fun collectState() {
        collectAsStateWithLifecycle(viewModel.userState) { state ->
            if (state !is UiState.Loading) {
                binding.apply {
                    progressBar.visibility = View.GONE
                }
            }

            when (state) {
                is UiState.Loading -> {
                    binding.apply {
                        progressBar.visibility = View.VISIBLE
                        layoutMain.visibility = View.GONE
                    }
                }

                is UserProfileUiState.UserInfoLoaded -> {
                    val address = "${state.data.address?.street}, ${state.data.address?.city}"
                    binding.apply {
                        tvName.text = state.data.name
                        tvAddress.text = address
                        layoutMain.visibility = View.VISIBLE
                    }
                }
            }
        }


        collectAsStateWithLifecycle(viewModel.albumsState) { state ->
            if (state !is UiState.Loading) {
                binding.progressBar.visibility = View.GONE
            }

            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is UserProfileUiState.UserAlbumsLoaded -> {
                    binding.rvAlbums.adapter = albumsAdapter
                    albumsAdapter.submitList(state.data)
                }
            }
        }

        collectAsStateWithLifecycle(viewModel.profileError) { state ->
            when (state) {
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.root.showSnackbar("${state.error}")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvAlbums.adapter = null
        _binding = null
    }

}