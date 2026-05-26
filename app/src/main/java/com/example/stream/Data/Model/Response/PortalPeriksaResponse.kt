package com.example.stream.Data.Model.Response

data class PortalPeriksaResponse(
    val riwayat: List<RiwayatItem>,
    val nama_anggota: String
)

data class RiwayatItem(
    val id: Int,
    val tanggal: String,
    val lokasi_pelaksanaan: String,
    val judul_berita: String
)

data class AnggotaResponse(
    val nama_anggota_keluarga: String,
    val posisi_keluarga: String,
    val anggota_keluarga_nik: String
)

data class PemeriksaanResponse(
    val nama_pasien: String?,
    val posyandu:String?,
    val alamat_posyandu: String?,
    val tinggi_badan: Float?,
    val berat_badan: Float?,
    val PMT: String?,
    val total_PMT: Float?,
    val ASI: Boolean?,
    val vit: String?,
    val imunisasi: String?
)