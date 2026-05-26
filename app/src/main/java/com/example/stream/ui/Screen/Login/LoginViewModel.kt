package com.example.stream.ui.Screen.Login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stream.Data.Local.UserPreferences
import com.example.stream.Data.Model.Request.LoginRequest
import com.example.stream.Data.Remote.Client.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginViewModel : ViewModel() {
    // State input form login
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var captchaInput = mutableStateOf("")

    // State URL Captcha dinamis
    private val _captchaUrl = MutableStateFlow("")
    val captchaUrl: StateFlow<String> = _captchaUrl.asStateFlow()

    // State status proses login (Idle, Loading, Success, Error)
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _showErrorToast = MutableStateFlow(false)
    val showErrorToast = _showErrorToast.asStateFlow()

    private val _errorMessage = MutableStateFlow("Email atau Password salah!")
    val errorMessage = _errorMessage.asStateFlow()

    init {
        loadCaptcha()
    }

    fun loadCaptcha() {
        val baseUrl = "https://koryuz.com/"
        val timestamp = System.currentTimeMillis()
        _captchaUrl.value = "${baseUrl}api/captcha?t=$timestamp"
        captchaInput.value = ""
    }

    fun resetErrorToast() {
        _showErrorToast.value = false
    }

    fun login(context: Context) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            try {
                val response = ApiClient.apiService.login(
                    LoginRequest(
                        email = email.value.trim(),
                        password = password.value,
                        captcha = captchaInput.value.trim()
                    )
                )

                if (response.isSuccessful) {
                    val loginResponse = response.body()

                    if (loginResponse != null && loginResponse.status == "success") {
                        _showErrorToast.value = false
                        Log.d("LoginSuccess", "Token didapatkan: ${loginResponse.access_token}")

                        UserPreferences.saveUserData(
                            context = context,
                            token = "Bearer ${loginResponse.access_token}",
                            nik = "-",
                            noKK = "-",
                            userId = 0,
                            email = email.value.trim(),
                            channel_name = "-",
                            nama = "User Posyandu"
                        )

                        _loginState.value = LoginState.Success(token = loginResponse.access_token)
                    } else {
                        // Jika server merespon 200 tetapi field status lain (gagal)
                        val msg = loginResponse?.message ?: "Gagal masuk ke sistem"
                        _loginState.value = LoginState.Error(msg)
                        _errorMessage.value = msg
                        _showErrorToast.value = true
                        loadCaptcha()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorCode = response.code()
                    Log.e("LoginError", "Gagal login server: $errorCode - $errorBody")

                    val parsedMessage = try {
                        JSONObject(errorBody ?: "").optString("message", "Email, Password, atau Captcha salah!")
                    } catch (e: Exception) {
                        "Email, Password, atau Captcha salah!"
                    }

                    _errorMessage.value = parsedMessage
                    _loginState.value = LoginState.Error(parsedMessage)
                    _showErrorToast.value = true
                    loadCaptcha()
                }
            } catch (e: Exception) {
                Log.e("LoginException", "Exception saat login: ${e.message}", e)
                _errorMessage.value = "Terjadi kesalahan koneksi jaringan"
                _loginState.value = LoginState.Error("Terjadi kesalahan: ${e.message}")
                _showErrorToast.value = true
                loadCaptcha()
            }
        }
    }
}

// Sealed class untuk mengontrol state UI di LoginScreen.kt
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}