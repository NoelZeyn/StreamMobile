package com.example.stream.Data.Model.Response

data class PortalProfileResponse (
    val status: String,
    val message: String,
    val data: PortalProfileResponseData?
)

data class PortalProfileResponseData(
    val id: Int,
    val name: String?,
    val channel_name: String?,
    val nik: String?,
    val posisi_keluarga: String?,
    val tanggal_lahir: String?,
    val jenis_kelamin: String?
)

data class UpdateEmailResponse(
    val status: String,
    val message: String,
    val data: UserEmailResponse? = null,
    val errors: Map<String, List<String>>? = null
)

data class UserEmailResponse(
    val id: Int,
    val email: String,
    val name: String?
)

data class WargaResponse(
    val id: Int,
    val name: String?,
    val nama_anggota_keluarga: String?,
    val channel_name: String?,
    val nik: String?,
    val posisi_keluarga: String?,
    val tanggal_lahir: String?,
    val jenis_kelamin: String?
)

data class UpdatePasswordResponse(
    val message: String
)

data class PosyanduDetailResponse(
    val nama_posyandu: String,
    val alamat: String,
    val jumlah_keluarga: Int
)