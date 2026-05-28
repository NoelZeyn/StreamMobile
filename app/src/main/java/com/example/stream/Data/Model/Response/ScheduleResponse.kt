package com.example.stream.Data.Model.Response

import android.os.Message
import com.google.gson.annotations.SerializedName

data class ScheduleResponse(
    @SerializedName("status") val status : Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<ScheduleItem>
)

data class ScheduleItem(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("status") val status: String

)