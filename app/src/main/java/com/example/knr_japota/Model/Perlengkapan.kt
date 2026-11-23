package com.example.knr_japota.Model

import com.google.gson.annotations.SerializedName

data class Perlengkapan(
    val id: Long? = null,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("nama_barang")
    val namaPerlengkapan: String,

    val jumlah: Int,

    val harga: Int,

    @SerializedName("total_harga")
    val totalHarga: Int,

    @SerializedName("created_at")
    val createdAt: String? = null
)
