package com.example.stream.Data.Model.Response

import com.google.gson.annotations.SerializedName

data class LiveScheduleResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<LiveScheduleItem>
)

data class LiveScheduleItem(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("status") val status: String
)