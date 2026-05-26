package com.example.stream.ui.Screen.PortalPeriksa

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stream.Data.Model.Response.AnggotaResponse
import com.example.stream.Data.Model.Response.PemeriksaanResponse
import com.example.stream.Data.Model.Response.RiwayatItem
import com.example.stream.Data.Remote.Repository.PortalPeriksa.PortalPeriksaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PortalPeriksaViewModel(
    private val repository: PortalPeriksaRepository
): ViewModel() {
    private val _riwayat = MutableStateFlow<List<RiwayatItem>>(emptyList())
    val riwayat: StateFlow<List<RiwayatItem>> get() = _riwayat

    private val _namaAnggota = MutableStateFlow("")
    val namaAnggota: StateFlow<String> get() = _namaAnggota

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun fetchRiwayat(Bearer: MutableState<String>, wargaId: Int, nik: String, tipe: String){
        viewModelScope.launch {
            _isLoading.value = true
            val result= repository.getRiwayat(Bearer, wargaId, nik, tipe)
            result.onSuccess {
                _riwayat.value = it.riwayat
                _namaAnggota.value = it.nama_anggota
            }.onFailure {
                _errorMessage.value = it.message
            }
            _isLoading.value = false
        }
    }

    private val _anggota = MutableStateFlow<List<AnggotaResponse>>(emptyList())
    val anggota: StateFlow<List<AnggotaResponse>> get() = _anggota

    fun fetchAnggota(Bearer: String, noKk: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.getAnggotaKeluarga(Bearer, noKk)
            result.onSuccess {
                _anggota.value = it
            }.onFailure {
                _errorMessage.value = it.message
            }
            _isLoading.value = false
        }
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun fetchPemeriksaan(bearer: String, id: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = repository.getPemeriksaan(bearer, id)
            _uiState.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unknown error") }
            )
        }
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: PemeriksaanResponse) : UiState()
    data class Error(val message: String) : UiState()
}