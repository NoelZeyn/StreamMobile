package com.example.stream.ui.Screen.EKMS

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EKMSViewModelFactory (
    private val repository: EKMSRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EKMSViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EKMSViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}