
package com.example.knr_japota.Sewa

import java.util.Locale

data class PerlengkapanModel(
    val nama: String,
    val harga: Int,
    val kategori: String,
    val ikon: Int,
    var dipilih: Boolean = false
)

fun formatRupiah(nilai: Int): String =
    "Rp " + String.format(Locale("id", "ID"), "%,d", nilai)
