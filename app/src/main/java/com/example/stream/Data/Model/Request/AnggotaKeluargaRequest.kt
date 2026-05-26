package com.example.stream.Data.Model.Request

data class AnggotaKeluargaRequest (
    val nik: String,
    val no_kk: String,
    val nama_anggota_keluarga: String,
    val keluarga_no_kk: String,
    val posisi_keluarga: String,
    val tanggal_lahir: String,
    val jenis_kelamin: String,
    val anak_ke: Int
)

enum class PosisiKeluarga(val value: String){
    ISTRI("Istri"),
    ANAK("Anak")
}

enum class JenisKelamin(val value: String){
    LAKI("Laki-laki"),
    PEREMPUAN("Perempuan")
}