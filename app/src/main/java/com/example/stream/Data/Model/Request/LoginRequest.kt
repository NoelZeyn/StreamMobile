package com.example.stream.Data.Model.Request

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("captcha") val captcha: String
)