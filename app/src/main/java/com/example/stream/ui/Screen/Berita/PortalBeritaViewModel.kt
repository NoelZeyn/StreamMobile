package com.example.stream.ui.Screen.Berita

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stream.Data.Model.Request.DaftarAntrianRequest
import com.example.stream.Data.Model.Response.AnggotaBeritaItem
import com.example.stream.Data.Model.Response.AnggotaTerdaftarResponse
import com.example.stream.Data.Model.Response.Antrian
import com.example.stream.Data.Model.Response.BeritaDetailItem
import com.example.stream.Data.Model.Response.DaftarAntrianResponse
import com.example.stream.Data.Model.Response.PortalBeritaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import org.json.JSONObject
import retrofit2.HttpException

class PortalBeritaViewModel(
    private val repository: PortalBeritaRepository
) : ViewModel() {
    private val _toastMessage = mutableStateOf<String?>(null)
    val toastMessage: State<String?> = _toastMessage

    fun resetToastMessage() {
        _toastMessage.value = null
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun fetchBerita(bearer: String, id: String?) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = repository.getBerita(bearer, id)
            _uiState.value = result.fold(
                onSuccess = { UiState.Success<Any>(it.data) },
                onFailure = { UiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    private val _uiStateDetail = MutableStateFlow<UiStateDetail>(UiStateDetail.Loading)
    val uiStateDetail: StateFlow<UiStateDetail> = _uiStateDetail

    fun fetchBeritaDetail(bearer: String, id: Int) {
        viewModelScope.launch {
            _uiStateDetail.value = UiStateDetail.Loading
            val result = repository.getBeritaDetail(bearer, id)
            _uiStateDetail.value = result.fold(
                onSuccess = { UiStateDetail.Success(it.data) },
                onFailure = { UiStateDetail.Error(it.message ?: "Unknown error") }
            )
        }
    }

    private val _uiStateAntrian = MutableStateFlow<UiStateAntrian>(UiStateAntrian.Loading)
    val uiStateAntrian: StateFlow<UiStateAntrian> = _uiStateAntrian

    fun fetchBeritaAntrian(bearer: String, beritaId: Int) {
        viewModelScope.launch {
            _uiStateAntrian.value = UiStateAntrian.Loading
            val result = repository.getBeritaAntrian(bearer, beritaId)
            _uiStateAntrian.value = result.fold(
                onSuccess = {
                    Log.d("AntrianSuccess", "Nomor: ${it.nomor}")
                    UiStateAntrian.Success(it)
                },
                onFailure = {
                    Log.e("AntrianError", it.message ?: "Unknown error")
                    UiStateAntrian.Error(it.message ?: "Unknown error")
                }
            )
        }
    }

    private val _uiStateDaftar = MutableStateFlow<DaftarAntrianUiState>(DaftarAntrianUiState.Idle)
    val uiStateDaftar: StateFlow<DaftarAntrianUiState> = _uiStateDaftar

    fun daftarAntrian(
        bearer: String,
        wargaNik: String,
        nik: String,
        beritaId: Int,
        tipePemeriksaan: String
    ) {
        viewModelScope.launch {
            _uiStateDaftar.value = DaftarAntrianUiState.Loading

            val request = DaftarAntrianRequest(
                warga_nik = wargaNik,
                nik = nik,
                berita_id = beritaId,
                tipe_pemeriksaan = tipePemeriksaan
            )

            val result = repository.daftarAntrian("Bearer $bearer", request)

            _uiStateDaftar.value = result.fold(
                onSuccess = { response ->
                    DaftarAntrianUiState.Success(response)
                },
                onFailure = { throwable ->
                    val message = when (throwable) {
                        is HttpException -> {
                            val errorBody = throwable.response()?.errorBody()?.string()
                            try {
                                JSONObject(errorBody).optString("message", "Terjadi kesalahan")
                            } catch (e: Exception) {
                                "Terjadi kesalahan"
                            }
                        }
                        else -> throwable.message ?: "Terjadi kesalahan"
                    }

                    _toastMessage.value = message
                    DaftarAntrianUiState.Error(message)
                }
            )
        }
    }

    fun resetUiStateDaftar() {
        _uiStateDaftar.value = DaftarAntrianUiState.Idle
    }

    private val _uiStateAnggota = MutableStateFlow<UiStateAnggota>(UiStateAnggota.Loading)
    val uiStateAnggota: StateFlow<UiStateAnggota> = _uiStateAnggota

    fun fetchAnggotaBerita(bearer: String, no_kk: String) {
        viewModelScope.launch {
            _uiStateAnggota.value = UiStateAnggota.Loading
            val result = repository.getAnggotaBerita(bearer, no_kk)
            _uiStateAnggota.value = result.fold(
                onSuccess = { UiStateAnggota.Success(it.data) },
                onFailure = { UiStateAnggota.Error(it.message ?: "Unknown error") }
            )
        }
    }

    private val _uiStateTerdaftar = MutableStateFlow<UiStateTerdaftar>(UiStateTerdaftar.Loading)
    val uiStateTerdaftar: StateFlow<UiStateTerdaftar> = _uiStateTerdaftar

    fun fetchAnggotaTerdaftar(bearer: String, acaraId: Int, wargaId: Int) {
        viewModelScope.launch {
            _uiStateTerdaftar.value = UiStateTerdaftar.Loading
            val result = repository.getAnggotaTerdaftar(bearer, acaraId, wargaId)
            _uiStateTerdaftar.value = result.fold(
                onSuccess = { UiStateTerdaftar.Success(it) },
                onFailure = { UiStateTerdaftar.Error(it.message ?: "Unknown error") }
            )
        }
    }
}


sealed class UiState {
    object Loading : UiState()
    data class Success<T>(val data: List<PortalBeritaItem>) : UiState()
    data class Error(val message: String) : UiState()
}

sealed class UiStateDetail {
    object Loading : UiStateDetail()
    data class Success(val data: BeritaDetailItem) : UiStateDetail()
    data class Error(val message: String) : UiStateDetail()
}

sealed class UiStateAntrian {
    object Loading : UiStateAntrian()
    data class Success(val data: Antrian) : UiStateAntrian()
    data class Error(val message: String) : UiStateAntrian()
}

sealed class DaftarAntrianUiState {
    object Idle : DaftarAntrianUiState()
    object Loading : DaftarAntrianUiState()
    data class Success(val data: DaftarAntrianResponse) : DaftarAntrianUiState()
    data class Error(val message: String) : DaftarAntrianUiState()
}

sealed class UiStateAnggota {
    object Loading : UiStateAnggota()
    data class Success(val data: List<AnggotaBeritaItem>) : UiStateAnggota()
    data class Error(val message: String) : UiStateAnggota()
}

sealed class UiStateTerdaftar {
    object Loading : UiStateTerdaftar()
    data class Success(val data: AnggotaTerdaftarResponse) : UiStateTerdaftar()
    data class Error(val message: String): UiStateTerdaftar()
}