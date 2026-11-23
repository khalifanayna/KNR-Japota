package com.example.knr_japota

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.knr_japota.databinding.ActivityRegisterBinding
import com.example.knr_japota.Model.AuthRequest
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.knr_japota.SupabaseConfig

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnDaftar.setOnClickListener {
            // 1. ambil isi inputan
            val nama = binding.etNama.text.toString().trim()
            val noHp = binding.etNoHp.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // 2. cek kosong
            if (nama.isEmpty() || noHp.isEmpty() ||
                email.isEmpty() || username.isEmpty() || password.isEmpty()
            ) {
                Toast.makeText(this, "Semua data wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. kirim data ke halaman Verifikasi
            lifecycleScope.launch {
                try {
                    val authApi = SupabaseConfig.createAuthApi()
                    val response = authApi.register(
                        AuthRequest(
                            email = email,
                            password = password
                        )
                    )
                    if (response.isSuccessful) {
                        val registerPref = getSharedPreferences(
                            "register_pref",
                            MODE_PRIVATE)
                        registerPref.edit()
                            .putString("username", username)
                            .putString("email", email)
                            .putString("nama", nama)
                            .putString("no_hp", noHp)
                            .apply()
                        Toast.makeText(
                            this@RegisterActivity,
                            "Pendaftaran akun berhasil!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Gagal mendaftar: ${response.errorBody()?.string()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Terjadi kesalahan: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            binding.tvKeLogin.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
        }
    }
}