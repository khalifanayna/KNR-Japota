package com.example.knr_japota.Model

import com.google.gson.annotations.SerializedName

data class Booking(
    val id: Long? = null,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("nama_lapangan")
    val namaLapangan: String,

    val tanggal: String,

    @SerializedName("jam_mulai")
    val jamMulai: String,

    @SerializedName("jam_selesai")
    val jamSelesai: String,

    val harga: Int,

    val status: String = "Menunggu",

    @SerializedName("created_at")
    val createdAt: String? = null
)