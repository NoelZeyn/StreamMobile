package com.example.stream.ui.Screen.Profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileViewModelFactory(private val repo: ProfileRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfilViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}