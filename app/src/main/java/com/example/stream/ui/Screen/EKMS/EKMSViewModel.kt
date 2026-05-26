package com.example.stream.ui.Screen.EKMS

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stream.Data.Model.Response.PemeriksaanData
import com.example.stream.Data.Model.Response.PortalEkmsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EKMSViewModel(private val repo: EKMSRepository) : ViewModel(){
    private val _pemeriksaanList = MutableStateFlow<PemeriksaanUiState>(PemeriksaanUiState.Loading)
    val pemeriksaanList: StateFlow<PemeriksaanUiState> = _pemeriksaanList

    fun getEkms(token: String, nik: String, definisi: String) {
        viewModelScope.launch {
            _pemeriksaanList.value = PemeriksaanUiState.Loading
            try {
                val data = repo.getEkms(token, nik, definisi)
                _pemeriksaanList.value = PemeriksaanUiState.Success(data)
            } catch (e: Exception) {
                _pemeriksaanList.value = PemeriksaanUiState.Error(e.message ?: "Terjadi Kesalahan")
            }
        }
    }

    private val _perkembanganData = MutableStateFlow<PerkembanganUiState>(PerkembanganUiState.Loading)
    val perkembanganData: StateFlow<PerkembanganUiState> = _perkembanganData

    fun getPerkembangan(token: String, nik: String) {
        viewModelScope.launch {
            _perkembanganData.value = PerkembanganUiState.Loading
            try {
                val data = repo.getPerkembangan(token, nik)
                _perkembanganData.value = PerkembanganUiState.Success(data)
            } catch (e: Exception) {
                _perkembanganData.value = PerkembanganUiState.Error(e.message ?: "Terjadi Kesalahan")
            }
        }
    }

    private val _loadData = MutableStateFlow<LoadUiState>(LoadUiState.Loading)
    val loadUiState: StateFlow<LoadUiState> = _loadData

    fun loadData(token: String, nik: String) {
        viewModelScope.launch {
            _loadData.value = LoadUiState.Loading
            try {
                val result = repo.getDataDiriEkms(token, nik)
                result.fold(
                    onSuccess = { data ->
                        _loadData.value = LoadUiState.Success(data)
                    },
                    onFailure = { throwable ->
                        _loadData.value = LoadUiState.Error(throwable.message ?: "Terjadi Kesalahan")
                    }
                )
            } catch (e: Exception) {
                _loadData.value = LoadUiState.Error(e.message ?: "Terjadi Kesalahan")
            }
        }
    }
}

sealed class PemeriksaanUiState {
    object Loading : PemeriksaanUiState()
    data class Success(val data: List<PemeriksaanData>) : PemeriksaanUiState()
    data class Error(val message: String) : PemeriksaanUiState()
}

sealed class PerkembanganUiState {
    object Loading : PerkembanganUiState()
    data class Success(val data: List<PemeriksaanData>) : PerkembanganUiState()
    data class Error(val message: String) : PerkembanganUiState()
}

sealed class LoadUiState {
    object Loading: LoadUiState()
    data class Success(val data: PortalEkmsResponse) : LoadUiState()
    data class Error(val message: String) : LoadUiState()
}