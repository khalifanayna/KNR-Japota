package com.example.knr_japota.Model

import com.google.gson.annotations.SerializedName

data class BookingUpdateRequest(
    val tanggal: String,

    @SerializedName("jam_mulai")
    val jamMulai: String,

    @SerializedName("jam_selesai")
    val jamSelesai: String
)
