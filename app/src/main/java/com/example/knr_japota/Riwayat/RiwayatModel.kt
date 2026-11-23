package com.example.knr_japota.Riwayat

data class RiwayatModel(
    val lapangan: String,
    val lokasi: String,
    val jadwal: String,
    val harga: String,
    val status: String,
    val foto: Int,
    val urutan: String = "",

    // Identitas transaksi untuk CRUD
    val jenis: String = "",
    val transaksiId: Long? = null,

    // Data asli booking
    val tanggalRaw: String = "",
    val jamMulaiRaw: String = "",
    val jamSelesaiRaw: String = "",

    // Data asli penyewaan
    val jumlah: Int? = null,
    val hargaSatuan: Int? = null
)
