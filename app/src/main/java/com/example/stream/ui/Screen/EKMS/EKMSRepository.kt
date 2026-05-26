package com.example.stream.ui.Screen.EKMS

import com.example.stream.Data.Model.Response.PemeriksaanData
import com.example.stream.Data.Model.Response.PortalEkmsResponse
import com.example.stream.Data.Remote.Client.ApiClient
import com.example.stream.Data.Remote.Service.ApiService

class EKMSRepository {
    suspend fun getEkms(token: String, nik: String, definisi: String): List<PemeriksaanData> {
        val response = ApiClient.apiService.getEkms(token, nik, definisi)
        if (response.isSuccessful) {
            return response.body()?.data ?: emptyList()
        } else {
            throw Exception("Error fetching data")
        }
    }

    suspend fun getPerkembangan(token: String, nik: String): List<PemeriksaanData> {
        val response = ApiClient.apiService.getPerkembangan(token, nik)
        if (response.isSuccessful) {
            return response.body()?.data ?: emptyList()
        } else {
            throw Exception("Error fetching data")
        }
    }

    suspend fun getDataDiriEkms(token: String, nik: String): Result<PortalEkmsResponse> {
        return try {
            val response = ApiClient.apiService.getDataDiriEkms(token, nik)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Gagal mengambil data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}