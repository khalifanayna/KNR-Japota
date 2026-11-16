package com.example.knr_japota.Notifikasi

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.knr_japota.databinding.ActivityNotifikasiBinding

class NotifikasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotifikasiBinding

    private val notifikasiList = listOf(
        NotifikasiModel("Pengingat Jadwal Main", "Besok pukul 19:00 kamu bermain di Lapangan 1", "Pengingat"),
        NotifikasiModel("GOR Tutup Lebih Awal", "Jumat, 20 Nov 2026 GOR tutup pukul 18:00", "Pengumuman"),
        NotifikasiModel("Tarif Lapangan", "Tarif sewa Rp 35.000 per jam berlaku untuk semua lapangan", "Pengumuman"),
        NotifikasiModel("Password Diperbarui", "Password akunmu berhasil diubah", "Akun")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Notifikasi"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val adapter = NotifikasiAdapter(this, notifikasiList)
        binding.listNotifikasi.adapter = adapter
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