package com.example.albumsviewer.features.albumDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.albumsviewer.databinding.PhotosFragmentBinding
import com.example.albumsviewer.result.UiState
import com.example.albumsviewer.utils.PagingLoadingStateAdapter
import com.example.albumsviewer.utils.collectAsStateWithLifecycle
import com.example.albumsviewer.utils.collectLatestAsStateWithLifecycle
import com.example.albumsviewer.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotosFragment : Fragment() {

    private val viewModel: PhotosViewModel by viewModels()

    private var _binding: PhotosFragmentBinding? = null
    private val binding get() = _binding!!

    private val photosAdapter: PhotosAdapter by lazy {
        PhotosAdapter { url ->
            findNavController().navigate(
                PhotosFragmentDirections.actionPhotosFragmentToPhotoViewerFragment(
                    url
                )
            )
        }.apply {
            withLoadStateFooter(
                footer = PagingLoadingStateAdapter { photosAdapter.retry() }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PhotosFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleUi()
        collectState()
    }

    private fun handleUi() {
        binding.apply {
            ivBack.setOnClickListener { findNavController().navigateUp() }
            rvPhotos.adapter = photosAdapter

            searchView.setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        binding.rvPhotos.scrollToPosition(0)
                        lifecycleScope.launch {
                            delay(500)
                            viewModel.searchPhotos(newText)                        }
                    }
                    return true
                }

            })
        }
    }

    private fun collectState() {
        collectLatestAsStateWithLifecycle(photosAdapter.loadStateFlow) { loadState ->
            viewModel.onPagingStateChanged(loadState, photosAdapter.itemCount)
        }

        collectAsStateWithLifecycle(viewModel.photosState) { state ->
            if (state !is UiState.Loading) {
                binding.progressBar.visibility = View.GONE
            }

            when (state) {
                is UiState.Loading -> {
                    binding.apply {
                        progressBar.visibility = View.VISIBLE
                        layoutPhotos.visibility = View.GONE
                        tvNoImages.visibility = View.GONE
                    }
                }

                is PhotosUiState.PhotosLoaded -> {
                    binding.rvPhotos.adapter = photosAdapter.withLoadStateFooter(
                        footer = PagingLoadingStateAdapter { photosAdapter.retry() }
                    )
                    photosAdapter.submitData(lifecycle, state.data)
                }

                is PhotosUiState.OnPhotosLoadedSuccessfully -> {
                    binding.apply {
                        layoutPhotos.visibility = View.VISIBLE
                        tvNoImages.visibility = View.GONE
                    }
                }

                is PhotosUiState.OnEmptyPhotosList->{
                    binding.apply {
                        layoutPhotos.visibility = View.GONE
                        tvNoImages.visibility = View.VISIBLE
                    }
                }
            }
        }

        collectAsStateWithLifecycle(viewModel.photosError) { state ->
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
        binding.rvPhotos.adapter = null
        _binding = null
    }

}