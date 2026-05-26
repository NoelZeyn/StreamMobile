package com.example.stream.Data.Remote.Repository.PortalPeriksa

import androidx.compose.runtime.MutableState
import com.example.stream.Data.Model.Response.AnggotaResponse
import com.example.stream.Data.Model.Response.PemeriksaanResponse
import com.example.stream.Data.Model.Response.PortalPeriksaResponse
import com.example.stream.Data.Remote.Client.ApiClient
import com.example.stream.Data.Remote.Client.ApiClient.apiService

class PortalPeriksaRepository {
    suspend fun getRiwayat(Bearer: MutableState<String>, wargaId: Int, nik: String, tipe: String): Result<PortalPeriksaResponse> {
        return try {
            val response = apiService.getRiwayat(Bearer, wargaId, nik, tipe)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Gagal: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAnggotaKeluarga(bearer: String, noKk: String): Result<List<AnggotaResponse>> {
        return try {
            val response = ApiClient.apiService.getAnggotaKeluarga(bearer, noKk)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Response kosong"))
            } else {
                Result.failure(Exception("Gagal: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPemeriksaan(bearer: String, id: Int): Result<PemeriksaanResponse> {
        return try {
            val response = apiService.getPemeriksaan(bearer, id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}