package com.example.stream.ui.Screen.PortalPeriksa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stream.Data.Remote.Repository.PortalPeriksa.PortalPeriksaRepository

class PortalPeriksaViewModelFactory(
    private val repository: PortalPeriksaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PortalPeriksaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PortalPeriksaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}