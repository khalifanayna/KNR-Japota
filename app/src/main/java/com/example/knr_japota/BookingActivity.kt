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

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private var tanggalDipilih: String? = null
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
            tanggalDipilih = it.replace("\n", " ")
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
            if (tanggalDipilih == null || jamDipilih == null) {
                Toast.makeText(this, "Pilih tanggal dan jam dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            MaterialAlertDialogBuilder(this)
                .setTitle("Booking Dikirim")
                .setMessage("$namaLapangan\n$tanggalDipilih\n$jamDipilih\nTotal: Rp 35.000\n\nMenunggu konfirmasi petugas.")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
                .show()
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

        val jumlahHari = 365
        repeat (jumlahHari) {
            hasil.add("${hari.format(kalender.time)}\n${tanggal.format(kalender.time)}\n${bulan.format(kalender.time)}")
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