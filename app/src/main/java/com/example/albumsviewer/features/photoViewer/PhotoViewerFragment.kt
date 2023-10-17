package com.example.albumsviewer.features.photoViewer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.albumsviewer.databinding.PhotoViewerFragmentBinding
import com.igreenwood.loupe.Loupe
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PhotoViewerFragment : Fragment() {

    private var _binding: PhotoViewerFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoViewerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PhotoViewerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleUi()
    }

    private fun handleUi() {
        if (viewModel.photoUrl != null) {
            binding.ivPhoto.load(viewModel.photoUrl)

            binding.ivShare.setOnClickListener {
                sharePhotoUrl(viewModel.photoUrl!!)
            }
        }

        Loupe.create(
            binding.ivPhoto,
            binding.imageViewerLayout
        ) {
            onViewTranslateListener = object : Loupe.OnViewTranslateListener {

                override fun onStart(view: ImageView) {}

                override fun onViewTranslate(view: ImageView, amount: Float) {}

                override fun onRestore(view: ImageView) {}

                override fun onDismiss(view: ImageView) {
                    findNavController().navigateUp()
                }
            }
        }

    }

    private fun sharePhotoUrl(url: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}