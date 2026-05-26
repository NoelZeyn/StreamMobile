package com.example.stream.Data.Model.Response

import java.util.Date

data class PortalBeritaItem(
    val id: Int,
    val judul: String,
    val tempat: String?,
    val tanggal: String
)

data class PortalBeritaResponse(
    val message: String,
    val data: List<PortalBeritaItem>
)

data class BeritaDetailResponse(
    val message: String,
    val data: BeritaDetailItem
)

data class BeritaDetailItem(
    val id: Int,
    val judul: String,
    val tempat: String?,
    val tanggal: String,
    val waktu: String,
    val deskripsi: String,
    val kategori: String
)

data class Antrian(
    val nomor: Int
)

data class DaftarAntrianResponse(
    val message: String,
    val nomor_antrian: Int,
    val pemeriksaan_id: Int
)

data class AnggotaBeritaItem(
    val nik: String,
    val nama_anggota_keluarga: String,
    val posisi_keluarga: String
)

data class AnggotaBeritaResponse(
    val message: String,
    val data: List<AnggotaBeritaItem>
)

data class AnggotaTerdaftarResponse(
    val message: String,
    val data: List<AnggotaTerdaftarItem>
)

data class AnggotaTerdaftarItem(
    val nama: String,
    val posisi: String,
    val nomor_antrian: Int
)