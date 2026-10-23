package com.example.knr_japota

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.knr_japota.databinding.ActivityVerifikasiBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class VerifikasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifikasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityVerifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. terima data dari halaman Registrasi
        val nama = intent.getStringExtra("nama") ?: ""
        val noHp = intent.getStringExtra("noHp") ?: ""
        val username = intent.getStringExtra("username") ?: ""
        val password = intent.getStringExtra("password") ?: ""

        binding.tvInfo.text = "Masukkan kode OTP untuk nomor $noHp"

        binding.btnVerifikasi.setOnClickListener {
            val otp = binding.etOtp.text.toString().trim()

            // 2. cek kosong
            if (otp.isEmpty()) {
                showError("Kode OTP wajib diisi")
                return@setOnClickListener
            }

            // 3. cek sama dengan nomor handphone
            if (otp != noHp) {
                showError("Kode OTP salah")
                return@setOnClickListener
            }

            // 4. berhasil -> simpan data registrasi ke SharedPreferences
            val sharedPref = getSharedPreferences("register_pref", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("nama", nama)
            editor.putString("noHp", noHp)
            editor.putString("username", username)
            editor.putString("password", password)
            editor.apply()

            Toast.makeText(this, "Registrasi berhasil, silakan login", Toast.LENGTH_SHORT).show()

            // 5. kembali ke Login & hapus halaman sebelumnya
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun showError(pesan: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Verifikasi Gagal")
            .setMessage(pesan)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}