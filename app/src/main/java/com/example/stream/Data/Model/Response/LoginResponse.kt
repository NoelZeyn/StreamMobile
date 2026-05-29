package com.example.stream.Data.Model.Response

data class LoginResponse(
    val status: String,
    val message: String,
    val access_token: String,
    val user_id: Int,
    val token_type: String
)

data class WargaData(
    val id: Int,
    val name: String,
    val email: String,
    val channel_name: String,
    val anggota_keluarga_nik: String,
    val keluarga_no_kk: String,
)
