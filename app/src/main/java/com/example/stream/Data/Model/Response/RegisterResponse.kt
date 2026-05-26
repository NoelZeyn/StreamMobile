package com.example.stream.Data.Model.Response

data class RegisterResponse (
    val message: String
)

data class PosyanduResponse<T>(
    val message: String,
    val data: T
)

data class PosyanduItem(
    val id: Int,
    val nama: String
)