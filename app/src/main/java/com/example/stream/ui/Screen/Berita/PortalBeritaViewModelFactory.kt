package com.example.stream.ui.Screen.Berita

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PortalBeritaViewModelFactory(
    private val repository: PortalBeritaRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PortalBeritaViewModel::class.java)) {
            return PortalBeritaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
