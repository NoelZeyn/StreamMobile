package com.example.stream.Data.Model.Response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<ProfileItem>
)

data class ProfileItem(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("channel_name") val channel_name: String,

)