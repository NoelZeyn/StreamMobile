package com.example.stream.ui.Screen.Profile

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stream.Data.Local.UserPreferences
import com.example.stream.Data.Model.Request.PortalProfileAnggotaRequest
import com.example.stream.Data.Model.Request.PortalProfileRequest
import com.example.stream.Data.Model.Request.UpdateEmailRequest
import com.example.stream.Data.Model.Request.UpdatePasswordRequest
import com.example.stream.Data.Model.Response.PortalProfileResponseData
import com.example.stream.Data.Model.Response.PosyanduDetailResponse
import com.example.stream.Data.Model.Response.ProfileResponse
import com.example.stream.Data.Model.Response.UpdateEmailResponse
import com.example.stream.Data.Model.Response.UpdatePasswordResponse
import com.example.stream.Data.Model.Response.WargaResponse
import com.example.stream.Data.Remote.Client.ApiClient
import com.example.stream.Data.Remote.Client.ApiClient.apiService
import com.example.stream.Data.Remote.Service.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfilViewModel(private val repository: ProfileRepository) : ViewModel() {

    var updateState by mutableStateOf<UpdateProfileState>(UpdateProfileState.Idle)
        private set

    fun updateProfile(request: PortalProfileRequest, id: Int, token: String) {
        viewModelScope.launch {
            updateState = UpdateProfileState.Loading

            val result = repository.updateProfile(request, id, token)
            updateState = result.fold(
                onSuccess = { data -> UpdateProfileState.Success(data) },
                onFailure = { e -> UpdateProfileState.Error(e.message ?: "Terjadi kesalahan") }
            )
        }
    }

    private val _profileState = MutableStateFlow<GetProfileState>(GetProfileState.Idle)
    val profileState: StateFlow<GetProfileState> = _profileState.asStateFlow()

    suspend fun getProfile(token: String): Result<ProfileResponse> {
        return try {
            val response = ApiClient.apiService.getProfile(token)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Gagal mengambil data profil: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun resetState() {
        updateState = UpdateProfileState.Idle
    }

    var updateEmailState by mutableStateOf<UpdateEmailState>(UpdateEmailState.Idle)
        private set

    fun updateEmail(request: UpdateEmailRequest, id: Int, token: String) {
        viewModelScope.launch {
            updateEmailState = UpdateEmailState.Loading

            val result = repository.updateEmail(request, id, token)
            updateEmailState = result.fold(
                onSuccess = { data -> UpdateEmailState.Success(data) },
                onFailure = { e -> UpdateEmailState.Error(e.message ?: "Terjadi kesalahan") }
            )
        }
    }

    fun resetEmailState() {
        updateEmailState = UpdateEmailState.Idle
    }

    var updatePasswordState by mutableStateOf<UpdatePasswordState>(UpdatePasswordState.Idle)
        private set

    fun updatePassword(request: UpdatePasswordRequest, id: Int, token: String) {
        viewModelScope.launch {
            updatePasswordState = UpdatePasswordState.Loading

            val result = repository.updatePassword(request, id, token)
            updatePasswordState = result.fold(
                onSuccess = { data -> UpdatePasswordState.Success(data) },
                onFailure = { e -> UpdatePasswordState.Error(e.message ?: "Terjadi kesalahan") }
            )
        }
    }

    fun resetPasswordState() {
        updatePasswordState = UpdatePasswordState.Idle
    }

    private val _profileAnggotaState = MutableStateFlow<GetAnggotaProfileState>(GetAnggotaProfileState.Idle)
    val profileAnggotaState: StateFlow<GetAnggotaProfileState> = _profileAnggotaState.asStateFlow()

    fun getAnggotaProfile(token: String, nik: String) {
        viewModelScope.launch {
            _profileAnggotaState.value = GetAnggotaProfileState.Loading
            val result = repository.getAnggotaProfile(token, nik)
            _profileAnggotaState.value = when {
                result.isSuccess -> GetAnggotaProfileState.Success(result.getOrThrow())
                result.isFailure -> GetAnggotaProfileState.Error(result.exceptionOrNull()?.message ?: "Terjadi kesalahan")
                else -> GetAnggotaProfileState.Idle
            }
        }
    }

    private val _updateAnggotaState = MutableStateFlow<UpdateProfileState>(UpdateProfileState.Idle)
    val updateAnggotaState: StateFlow<UpdateProfileState> = _updateAnggotaState.asStateFlow()

    fun updateAnggotaProfile(request: PortalProfileAnggotaRequest, nik: String, token: String) {
        viewModelScope.launch {
            _updateAnggotaState.value = UpdateProfileState.Loading
            val result = repository.updateProfileAnggota(request, nik, token)

            _updateAnggotaState.value = result.fold(
                onSuccess = { data -> UpdateProfileState.Success(data) },
                onFailure = { e -> UpdateProfileState.Error(e.message ?: "Terjadi kesalahan") }
            )
        }
    }

    private val _posyanduState = MutableStateFlow<PosyanduState>(PosyanduState.Loading)
    val posyanduState: StateFlow<PosyanduState> = _posyanduState

    fun fetchPosyandu(noKk: String, token: String) {
        viewModelScope.launch {
            _posyanduState.value = PosyanduState.Loading
            val result = repository.getPosyanduDetail(noKk, token)
            _posyanduState.value = when {
                result.isSuccess -> result.getOrNull()?.let {
                    PosyanduState.Success(it)
                } ?: PosyanduState.Error("Data posyandu kosong")
                result.isFailure -> PosyanduState.Error(result.exceptionOrNull()?.message ?: "Terjadi kesalahan")
                else -> PosyanduState.Error("Unknown error")
            }
        }
    }

    private val _logoutState = MutableStateFlow<Result<Boolean>?>(null)
    val logoutState: StateFlow<Result<Boolean>?> = _logoutState

    fun logout(context: Context, token: String) {
        viewModelScope.launch {
            try {
                val response = apiService.logout(token)
                if (response.isSuccessful) {
                    UserPreferences.clearUserData(context)
                    _logoutState.value = Result.success(true)
                } else {
                    _logoutState.value = Result.failure(Exception("Gagal logout dari server"))
                }
                Log.d("LogoutDebug", "Response: ${response.code()}, isSuccess: ${response.isSuccessful}")
            } catch (e: Exception) {
                _logoutState.value = Result.failure(e)
            }
        }
    }
}

sealed class UpdateProfileState {
    object Idle : UpdateProfileState()
    object Loading : UpdateProfileState()
    data class Success(val data: PortalProfileResponseData) : UpdateProfileState()
    data class Error(val message: String) : UpdateProfileState()
}

sealed class GetProfileState {
    object Idle : GetProfileState()
    object Loading : GetProfileState()
    data class Success(val data: WargaResponse) : GetProfileState()
    data class Error(val message: String) : GetProfileState()
}

sealed class GetAnggotaProfileState {
    object Idle : GetAnggotaProfileState()
    object Loading : GetAnggotaProfileState()
    data class Success(val data: WargaResponse) : GetAnggotaProfileState()
    data class Error(val message: String) : GetAnggotaProfileState()
}

sealed class UpdateEmailState {
    object Idle : UpdateEmailState()
    object Loading : UpdateEmailState()
    data class Success(val data: UpdateEmailResponse) : UpdateEmailState()
    data class Error(val message: String) : UpdateEmailState()
}

sealed class UpdatePasswordState {
    object Idle : UpdatePasswordState()
    object Loading : UpdatePasswordState()
    data class Success(val data: UpdatePasswordResponse) : UpdatePasswordState()
    data class Error(val message: String) : UpdatePasswordState()
}

sealed class PosyanduState {
    object Loading : PosyanduState()
    data class Success(val data: PosyanduDetailResponse) : PosyanduState()
    data class Error(val message: String) : PosyanduState()
}
