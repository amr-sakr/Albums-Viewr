package com.example.albumsviewer.features.photoViewer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val photoUrl = savedStateHandle.get<String?>("photo_url")
}