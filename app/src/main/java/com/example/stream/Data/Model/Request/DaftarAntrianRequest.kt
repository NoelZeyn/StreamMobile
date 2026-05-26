package com.example.stream.Data.Model.Request

data class DaftarAntrianRequest(
    val warga_nik: String,
    val nik: String,
    val berita_id: Int,
    val tipe_pemeriksaan: String
)