package com.example.stream.ui.Screen.Dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stream.Data.Model.Response.LiveScheduleResponse
import com.example.stream.Data.Model.Response.ScheduleItem
import com.example.stream.Data.Model.Response.ScheduleResponse
import com.example.stream.Data.Remote.Service.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ScheduleState {
    object Idle : ScheduleState()
    object Loading : ScheduleState()
    data class Success(val data: ScheduleResponse) : ScheduleState()
    data class Error(val message: String) : ScheduleState()
}

sealed class LiveState {
    object Idle : LiveState()
    object Loading : LiveState()
    data class Success(val data: LiveScheduleResponse) : LiveState()
    data class Error(val message: String) : LiveState()
}

data class ScheduleResponse(val schedules: List<ScheduleItem> = emptyList())
data class LiveScheduleResponse(val title: String, val hostName: String, val hostId: Int)

// --- 3. ViewModel ---
class DashboardViewModel(private val apiService: ApiService) : ViewModel() {

    private val _scheduleState = MutableStateFlow<ScheduleState>(ScheduleState.Idle)
    val scheduleState: StateFlow<ScheduleState> = _scheduleState.asStateFlow()

    private val _liveState = MutableStateFlow<LiveState>(LiveState.Idle)
    val liveState: StateFlow<LiveState> = _liveState.asStateFlow()

    fun fetchSchedules(userId: Int) {
        viewModelScope.launch {
            _scheduleState.value = ScheduleState.Loading
            try {
                val response = apiService.getSchedules(userId = userId)
                if (response.isSuccessful && response.body() != null) {
                    _scheduleState.value = ScheduleState.Success(response.body()!!)
                } else {
                    _scheduleState.value = ScheduleState.Error("Gagal memuat jadwal: ${response.code()}")
                }
            } catch (e: Exception) {
                _scheduleState.value = ScheduleState.Error(e.message ?: "Terjadi kesalahan jaringan")
            }
        }
    }

    fun fetchLiveStream(userId: Int) {
        viewModelScope.launch {
            _liveState.value = LiveState.Loading
            try {
                val response = apiService.getLive(userId = userId)
                if (response.isSuccessful && response.body() != null) {
                    _liveState.value = LiveState.Success(response.body()!!)
                } else {
                    _liveState.value = LiveState.Error("Gagal memuat live stream: ${response.code()}")
                }
            } catch (e: Exception) {
                _liveState.value = LiveState.Error(e.message ?: "Terjadi kesalahan jaringan")
            }
        }
    }
}