package com.example.knr_japota

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.knr_japota.databinding.ActivityBookingBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.lifecycle.lifecycleScope
import com.example.knr_japota.api.SupabaseApi
import com.example.knr_japota.Model.Booking
import kotlinx.coroutines.launch

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private var tanggalDipilih: String? = null
    private val tanggalIsoMap = mutableMapOf<String, String>()
    private var jamDipilih: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Detail Booking"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val namaLapangan = intent.getStringExtra("namaLapangan") ?: "Lapangan 1"
        binding.tvNamaLapangan.text = namaLapangan
        if (namaLapangan == "Lapangan 2") {
            binding.imgLapangan.setImageResource(R.drawable.lapangangor)
        } else {
            binding.imgLapangan.setImageResource(R.drawable.lapangangor)
        }

        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        binding.tvSapa.text = "Halo, ${sharedPref.getString("username", "Pemain")}!"

        binding.btnLokasi.setOnClickListener { bukaGoogleMaps("GOR Gabus Rumbai") }

        binding.rvTanggal.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTanggal.adapter = PilihanAdapter(buatDaftarTanggal(), R.layout.item_tanggal) {
            tanggalDipilih = tanggalIsoMap[it]
        }

        val daftarJam = listOf(
            "15:00 - 16:00", "17:00 - 18:00", "19:00 - 20:00",
            "21:00 - 22:00", "23:00 - 00:00"
        )
        binding.rvJam.layoutManager = GridLayoutManager(this, 2)
        binding.rvJam.adapter = PilihanAdapter(daftarJam, R.layout.item_jam) {
            jamDipilih = it
        }

        binding.btnBooking.setOnClickListener {
            val tanggal = tanggalDipilih
            val jam = jamDipilih
            if (tanggal == null || jam == null) {
                Toast.makeText(
                    this,
                    "Pilih tanggal dan jam dulu",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val sharedPref = getSharedPreferences(
                "user_pref",
                Context.MODE_PRIVATE
            )
            val userId = sharedPref.getString("user_id", null)
            val token = sharedPref.getString("access_token", null)
            if (userId.isNullOrEmpty() || token.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    "Silakan login kembali",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                try {
                    val api = SupabaseConfig.createRetrofit(token)
                        .create(SupabaseApi::class.java)

                    // Periksa jadwal booking milik pengguna
                    val cek = api.getBookings()

                    if (!cek.isSuccessful) {
                        Toast.makeText(
                            this@BookingActivity,
                            "Gagal memeriksa jadwal",
                            Toast.LENGTH_LONG
                        ).show()
                        return@launch
                    }
                    val jamMulai = jam.substringBefore(" - ")
                    val jamSelesai = jam.substringAfter(" - ")

                    val bentrok = cek.body().orEmpty().any {
                        it.namaLapangan == namaLapangan &&
                                it.tanggal == tanggal &&
                                it.jamMulai == jamMulai &&
                                it.jamSelesai == jamSelesai &&
                                it.status != "Selesai"
                    }
                    if (bentrok) {
                        Toast.makeText(
                            this@BookingActivity,
                            "Jadwal tersebut sudah kamu booking",
                            Toast.LENGTH_LONG
                        ).show()
                        return@launch
                    }
                    val booking = Booking(
                        userId = userId,
                        namaLapangan = namaLapangan,
                        tanggal = tanggal,
                        jamMulai = jamMulai,
                        jamSelesai = jamSelesai,
                        harga = 35000,
                        status = "Menunggu"
                    )
                    val response = api.createBooking(booking)
                    if (response.isSuccessful) {
                        MaterialAlertDialogBuilder(this@BookingActivity)
                            .setTitle("Booking Berhasil")
                            .setMessage(
                                "$namaLapangan\n$tanggal\n$jam\n" +
                                        "Total: Rp 35.000\n\n" +
                                        "Status: Menunggu konfirmasi."
                            )
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                finish()
                            }
                            .show()
                    } else {
                        Toast.makeText(
                            this@BookingActivity,
                            "Gagal booking: ${response.errorBody()?.string()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(
                        this@BookingActivity,
                        "Terjadi kesalahan: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    private fun bukaGoogleMaps(lokasi: String) {
        val query = Uri.encode(lokasi)
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$query"))
        try {
            startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/search/?api=1&query=$query")
            )
            try {
                startActivity(webIntent)
            } catch (e2: ActivityNotFoundException) {
                Toast.makeText(this, "Tidak ada aplikasi peta", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buatDaftarTanggal(): List<String> {
        val hasil = mutableListOf<String>()
        val kalender = Calendar.getInstance()
        val lokal = Locale("id", "ID")

        val hari = SimpleDateFormat("EEE", lokal)
        val tanggal = SimpleDateFormat("d", lokal)
        val bulan = SimpleDateFormat("MMM", lokal)
        val formatDatabase = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        repeat(365) {
            val tampilan =
                "${hari.format(kalender.time)}\n" +
                        "${tanggal.format(kalender.time)}\n" +
                        bulan.format(kalender.time)

            hasil.add(tampilan)
            tanggalIsoMap[tampilan] =
                formatDatabase.format(kalender.time)

            kalender.add(Calendar.DAY_OF_MONTH, 1)
        }

        return hasil
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}