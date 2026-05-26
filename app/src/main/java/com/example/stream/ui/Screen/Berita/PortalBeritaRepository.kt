package com.example.stream.ui.Screen.Berita

import com.example.stream.Data.Model.Request.DaftarAntrianRequest
import com.example.stream.Data.Model.Response.AnggotaBeritaResponse
import com.example.stream.Data.Model.Response.AnggotaTerdaftarResponse
import com.example.stream.Data.Model.Response.Antrian
import com.example.stream.Data.Model.Response.BeritaDetailResponse
import com.example.stream.Data.Model.Response.DaftarAntrianResponse
import com.example.stream.Data.Model.Response.PortalBeritaResponse
import com.example.stream.Data.Remote.Client.ApiClient

class PortalBeritaRepository {
    suspend fun getBerita(bearer: String, posyandu_id: String?): Result<PortalBeritaResponse> {
        return try {
            val response = ApiClient.apiService.getBerita(bearer, posyandu_id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBeritaDetail(bearer: String, berita_id: Int): Result<BeritaDetailResponse> {
        return try {
            val response = ApiClient.apiService.getBeritaDetail(bearer, berita_id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBeritaAntrian(bearer: String, berita_id: Int): Result<Antrian> {
        return try {
            val response = ApiClient.apiService.getBeritaAntrian(bearer, berita_id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun daftarAntrian(
        bearer: String,
        request: DaftarAntrianRequest
    ): Result<DaftarAntrianResponse> {
        return try {
            val response = ApiClient.apiService.daftarAntrian("Bearer $bearer", request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAnggotaBerita(bearer: String, no_kk: String): Result<AnggotaBeritaResponse> {
        return try {
            val response = ApiClient.apiService.getAnggotaBerita(bearer, no_kk)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAnggotaTerdaftar(bearer: String, acaraId: Int, wargaId: Int): Result<AnggotaTerdaftarResponse> {
        return try {
            val response = ApiClient.apiService.getAnggotaTerdaftar(bearer, acaraId, wargaId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}