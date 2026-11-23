package com.example.knr_japota.Sewa

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.knr_japota.R
import com.example.knr_japota.databinding.ActivitySewaBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.content.Context
import androidx.lifecycle.lifecycleScope
import com.example.knr_japota.SupabaseConfig
import com.example.knr_japota.api.SupabaseApi
import com.example.knr_japota.Model.Perlengkapan
import kotlinx.coroutines.launch
class SewaActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySewaBinding
    private lateinit var adapter: PerlengkapanAdapter

    private val semuaItem = listOf(
        PerlengkapanModel("Raket Yonex", 15000, "Alat", R.drawable.ic_badminton),
        PerlengkapanModel("Raket Victor", 15000, "Alat", R.drawable.ic_badminton),
        PerlengkapanModel("Shuttlecock (1 tabung)", 85000, "Alat", R.drawable.ic_badminton),
        PerlengkapanModel("Sepatu Olahraga", 10000, "Alat", R.drawable.ic_shoe),
        PerlengkapanModel("Kaos Kaki", 5000, "Alat", R.drawable.ic_shoe),
        PerlengkapanModel("Air Mineral", 5000, "Minuman", R.drawable.ic_water),
        PerlengkapanModel("Minuman Isotonik", 8000, "Minuman", R.drawable.ic_sparklingwater)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySewaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Sewa Perlengkapan"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // RecyclerView grid 2 kolom
        adapter = PerlengkapanAdapter(semuaItem) { perbaruiRingkasan() }
        binding.rvPerlengkapan.layoutManager = GridLayoutManager(this, 2)
        binding.rvPerlengkapan.adapter = adapter

        // Filter kategori dengan ChipGroup
        binding.chipGroupKategori.setOnCheckedStateChangeListener { _, checkedIds ->
            val daftar = when (checkedIds.firstOrNull()) {
                R.id.chipAlat -> semuaItem.filter { it.kategori == "Alat" }
                R.id.chipMinuman -> semuaItem.filter { it.kategori == "Minuman" }
                else -> semuaItem
            }
            adapter.updateData(daftar)
        }

        binding.btnSewa.setOnClickListener {

            val terpilih = semuaItem.filter { it.dipilih }

            if (terpilih.isEmpty()) {
                Toast.makeText(
                    this,
                    "Pilih perlengkapan dulu",
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

                    var semuaBerhasil = true

                    for (item in terpilih) {

                        val data = Perlengkapan(
                            userId = userId,
                            namaPerlengkapan = item.nama,
                            jumlah = 1,
                            harga = item.harga.toInt(),
                            totalHarga = item.harga.toInt()
                        )

                        val response = api.createPerlengkapan(data)

                        if (!response.isSuccessful) {
                            semuaBerhasil = false
                            Toast.makeText(
                                this@SewaActivity,
                                "Gagal menyimpan ${item.nama}: " +
                                        response.errorBody()?.string(),
                                Toast.LENGTH_LONG
                            ).show()
                            break
                        }
                    }

                    if (semuaBerhasil) {
                        val total = terpilih.sumOf { it.harga }

                        MaterialAlertDialogBuilder(this@SewaActivity)
                            .setTitle("Penyewaan Berhasil")
                            .setMessage(
                                terpilih.joinToString("\n") {
                                    "- ${it.nama}"
                                } +
                                        "\n\nTotal: ${formatRupiah(total)}" +
                                        "\n\nSilakan ambil perlengkapan di petugas GOR."
                            )
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                finish()
                            }
                            .show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(
                        this@SewaActivity,
                        "Terjadi kesalahan: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        perbaruiRingkasan()
    }

    private fun perbaruiRingkasan() {
        val terpilih = semuaItem.filter { it.dipilih }
        binding.tvRingkasan.text = if (terpilih.isEmpty()) {
            "Belum ada item dipilih"
        } else {
            "${terpilih.size} item | ${formatRupiah(terpilih.sumOf { it.harga })}"
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