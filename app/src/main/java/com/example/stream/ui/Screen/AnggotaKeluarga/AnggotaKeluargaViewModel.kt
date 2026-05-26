package com.example.stream.ui.Screen.AnggotaKeluarga

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stream.Data.Model.Request.AnggotaKeluargaRequest
import com.example.stream.Data.Model.Request.JenisKelamin
import com.example.stream.Data.Model.Request.PosisiKeluarga
import com.example.stream.Data.Model.Request.RegisterRequest
import com.example.stream.Data.Remote.Client.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class AnggotaKeluargaViewModel : ViewModel() {
    var nama_anggota_keluarga: String = ""
    var no_kk: String = ""
    var anggota_keluarga_nik: String = ""
    lateinit var posisi_keluarga: PosisiKeluarga
    var tanggalLahir: String = ""
    lateinit var jenis_kelamin: JenisKelamin
    var anak_ke: Int = 0

    private val _anggotaKeluargaState = MutableStateFlow<AnggotaKeluargaState>(AnggotaKeluargaState.Idle)
    val anggotaKeluargaState: StateFlow<AnggotaKeluargaState> = _anggotaKeluargaState

    fun saveNoKK(no_kk: String){
        this.no_kk = no_kk
    }

    suspend fun saveAnggotaKeluarga(
        nama_anggota_keluarga: String,
        anggota_keluarga_nik: String,
        tanggalLahir: LocalDate,
        jenisKelamin: JenisKelamin,
        anak_ke: Int
    ) {
        this.nama_anggota_keluarga = nama_anggota_keluarga
        this.anggota_keluarga_nik = anggota_keluarga_nik
        this.tanggalLahir = tanggalLahir.toString()
        this.jenis_kelamin = jenisKelamin
        this.anak_ke = anak_ke
    }

    fun savePosisiKeluarga(posisi_keluarga: PosisiKeluarga) {
        Log.d("TAG", "Saving status: $posisi_keluarga")
        this.posisi_keluarga = posisi_keluarga
    }

    fun save() {
        viewModelScope.launch {
            _anggotaKeluargaState.value = AnggotaKeluargaState.Loading

            try {
                val response = ApiClient.apiService.anggotaKeluarga(
                    AnggotaKeluargaRequest(
                        nik = anggota_keluarga_nik,
                        no_kk = no_kk,
                        nama_anggota_keluarga = nama_anggota_keluarga,
                        keluarga_no_kk = no_kk,
                        posisi_keluarga = posisi_keluarga.value,
                        tanggal_lahir = tanggalLahir,
                        jenis_kelamin = jenis_kelamin.value,
                        anak_ke = anak_ke
                    )
                )
                if (response.isSuccessful) {
                    _anggotaKeluargaState.value = AnggotaKeluargaState.Success("Register Berhasil")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorCode = response.code()
                    Log.e("AnggotaKeluarga", "Register gagal: $errorCode - $errorBody")
                    _anggotaKeluargaState.value = AnggotaKeluargaState.Error("Register gagal: $errorCode - $errorBody")
                }
            } catch (e: Exception) {
                _anggotaKeluargaState.value = AnggotaKeluargaState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }

}


sealed class AnggotaKeluargaState {
    object Idle : AnggotaKeluargaState()
    object Loading : AnggotaKeluargaState()
    data class Success(val message: String) : AnggotaKeluargaState()
    data class Error(val error: String) : AnggotaKeluargaState()
}