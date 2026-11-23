package com.example.knr_japota

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.knr_japota.databinding.ActivityLoginBinding
import com.example.knr_japota.Model.AuthRequest
import com.example.knr_japota.api.SupabaseApi
import com.example.knr_japota.Model.UserProfile
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences(
            "user_pref",
            Context.MODE_PRIVATE
        )

        val registerPref = getSharedPreferences(
            "register_pref",
            Context.MODE_PRIVATE
        )

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // Proses Login
        binding.btnSignIn1.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Email dan password wajib diisi",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS
                    .matcher(email).matches()
            ) {
                Toast.makeText(
                    this,
                    "Format email tidak valid",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {

                try {

                    // Menghubungkan ke Supabase Auth
                    val authApi = SupabaseConfig.createAuthApi()

                    val response = authApi.login(
                        request = AuthRequest(
                            email = email,
                            password = password
                        )
                    )

                    if (response.isSuccessful) {

                        val authResponse = response.body()
                        val accessToken = authResponse?.accessToken
                        val userId = authResponse?.user?.id

                        if (!accessToken.isNullOrEmpty() &&
                            !userId.isNullOrEmpty()
                        ) {

                            // Mengambil data registrasi
                            val nama = registerPref.getString("nama", "") ?: ""
                            val username = registerPref.getString("username", "") ?: ""
                            // Menghubungkan ke tabel mobile.users
                            val supabaseApi = SupabaseConfig
                                .createRetrofit(accessToken)
                                .create(SupabaseApi::class.java)
                            // Memeriksa profil pengguna
                            val profileResponse =
                                supabaseApi.getUserProfile(
                                    idFilter = "eq.$userId"
                                )
                            if (!profileResponse.isSuccessful) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Gagal memeriksa profil pengguna",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@launch
                            }
                            // Membuat profil jika belum tersedia
                            if (profileResponse.body().isNullOrEmpty()) {

                                val profile = UserProfile(
                                    id = userId,
                                    nama = nama,
                                    email = email,
                                    username = username,
                                    role = "user"
                                )
                                val saveResponse = supabaseApi.createUserProfile(profile)

                                if (!saveResponse.isSuccessful) {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Gagal menyimpan profil: ${
                                            saveResponse.errorBody()?.string()
                                        }",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return@launch
                                }
                            }

                            // Menyimpan sesi login
                            sharedPref.edit()
                                .putBoolean("isLogin", true)
                                .putString("email", email)
                                .putString("access_token", accessToken)
                                .putString("user_id", userId)
                                .apply()

                            Toast.makeText(
                                this@LoginActivity,
                                "Login berhasil!",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Masuk ke halaman utama
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    BaseActivity::class.java
                                )
                            )

                            finish()

                        } else {

                            Toast.makeText(
                                this@LoginActivity,
                                "Data autentikasi tidak lengkap",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } else {

                        Toast.makeText(
                            this@LoginActivity,
                            "Login gagal: ${
                                response.errorBody()?.string()
                            }",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@LoginActivity,
                        "Terjadi kesalahan: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // Menu menuju halaman registrasi
        binding.tvRegister.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }

        // Toolbar
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = "Back"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
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
