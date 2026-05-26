package com.example.stream.Data.Model.Request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("channel_name") val channelName: String, // Mengubah camelCase menjadi snake_case saat dikirim ke API
    @SerializedName("captcha") val captcha: String
)