package com.example.stream.Data.Model.Response

class PortalEkmsResponse (
    val nama: String,
    val umur: String,
    val berat_badan: Float?,
    val kategori: String
)

data class DataResponse(
    val data: List<PemeriksaanData>
)

data class PemeriksaanData(
    val umur: String,
    val tinggi_badan: Float?,
    val status_gizi: String?,
    val berat_badan: Float?,
    val imunisasi: String?,
    val tanggal: String?,
    val vitamin: String?,
    val suplemen: String?
)