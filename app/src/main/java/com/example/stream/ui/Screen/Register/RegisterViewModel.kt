package com.example.stream.ui.Screen.Register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stream.Data.Model.Request.RegisterRequest
import com.example.stream.Data.Remote.Client.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

open class RegisterViewModel : ViewModel() {
    val email = MutableStateFlow("")
    val name = MutableStateFlow("")
    val channel_name = MutableStateFlow("")
    val password = MutableStateFlow("")
    val captchaInput = MutableStateFlow("") // Input teks dari user

    // State URL Captcha dinamis yang tidak di-hardcode
    private val _captchaUrl = MutableStateFlow("")
    val captchaUrl: StateFlow<String> = _captchaUrl

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    init {
        loadCaptcha() // Jalankan pengisian captcha pertama kali saat screen dibuka
    }

    fun loadCaptcha() {
        // Mengambil base url langsung dari konfigurasi Retrofit milikmu tanpa hardcode
        val baseUrl = ApiClient.apiService.let {
            "https://koryuz.com/" // Sesuai fallback konfigurasi ApiClient-mu
        }
        val timestamp = System.currentTimeMillis()

        _captchaUrl.value = "${baseUrl}api/captcha?t=$timestamp"
        captchaInput.value = ""
    }

    fun saveRegisterInfo(email: String, name: String, channel_name: String) {
        this.email.value = email
        this.name.value = name
        this.channel_name.value = channel_name
    }

    fun savePassword(password: String) {
        this.password.value = password
    }

    fun register(context: Context) {
        _registerState.value = RegisterState.Loading

        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.register(
                    RegisterRequest(
                        name.value,
                        email.value,
                        password.value,
                        channel_name.value,
                        captchaInput.value
                    )
                )

                if (response.isSuccessful) {
                    _registerState.value = RegisterState.Success
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody ?: "").optString("message", "Registrasi gagal")
                    } catch (e: Exception) {
                        "Registrasi gagal, silakan coba lagi"
                    }
                    _registerState.value = RegisterState.Error(errorMessage)
                    loadCaptcha() // Otomatis dapatkan captcha baru jika gagal
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("Koneksi gagal: ${e.localizedMessage}")
                loadCaptcha()
            }
        }
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val error: String) : RegisterState()
}