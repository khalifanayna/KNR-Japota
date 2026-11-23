package com.example.knr_japota.Riwayat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.knr_japota.R
import com.example.knr_japota.SupabaseConfig
import com.example.knr_japota.api.SupabaseApi
import com.example.knr_japota.databinding.FragmentRiwayatBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class RiwayatFragment : Fragment() {

    private var _binding: FragmentRiwayatBinding? = null
    private val binding get() = _binding!!

    private val riwayatList = mutableListOf<RiwayatModel>()
    private lateinit var adapter: RiwayatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            "Riwayat Transaksi"

        adapter = RiwayatAdapter(riwayatList) { item ->
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Detail Transaksi")
                .setMessage(
                    "${item.lapangan}\n" +
                            "${item.lokasi}\n" +
                            "${item.jadwal}\n" +
                            "Total: ${item.harga}\n" +
                            "Status: ${item.status}"
                )
                .setPositiveButton("OK", null)
                .show()
        }

        binding.rvRiwayat.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRiwayat.adapter = adapter

        muatRiwayat()
    }

    private fun muatRiwayat() {
        val pref = requireContext().getSharedPreferences(
            "user_pref",
            android.content.Context.MODE_PRIVATE
        )

        val userId = pref.getString("user_id", null)
        val token = pref.getString("access_token", null)

        if (userId.isNullOrEmpty() || token.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                "Silakan login kembali",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val api = SupabaseConfig.createRetrofit(token)
                    .create(SupabaseApi::class.java)

                val bookingResponse = api.getBookings()
                val sewaResponse = api.getPerlengkapan()

                if (!bookingResponse.isSuccessful || !sewaResponse.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Gagal mengambil riwayat transaksi",
                        Toast.LENGTH_LONG
                    ).show()
                    return@launch
                }

                val daftarBooking = bookingResponse.body().orEmpty()
                val daftarSewa = sewaResponse.body().orEmpty()

                riwayatList.clear()

                // Mengubah data booking menjadi data tampilan
                daftarBooking.forEach { booking ->
                    riwayatList.add(
                        RiwayatModel(
                            lapangan = booking.namaLapangan,
                            lokasi = "GOR Gabus Rumbai",
                            jadwal = "${formatTanggal(booking.tanggal)} | " +
                                    "${booking.jamMulai} - ${booking.jamSelesai}",
                            harga = formatRupiah(booking.harga.toLong()),
                            status = booking.status,
                            foto = R.drawable.lapangangor,
                            urutan = booking.createdAt ?: booking.tanggal
                        )
                    )
                }

                // Mengubah data penyewaan menjadi data tampilan
                daftarSewa.forEach { sewa ->
                    riwayatList.add(
                        RiwayatModel(
                            lapangan = "Sewa: ${sewa.namaPerlengkapan}",
                            lokasi = "GOR Gabus Rumbai",
                            jadwal = "Tanggal transaksi: " +
                                    formatTanggal(sewa.createdAt?.take(10) ?: ""),
                            harga = formatRupiah(sewa.totalHarga.toLong()),
                            status = "Berhasil",
                            foto = R.drawable.lapangangor,
                            urutan = sewa.createdAt ?: ""
                        )
                    )
                }

                // Transaksi terbaru ditampilkan paling atas
                riwayatList.sortByDescending { it.urutan }

                adapter.notifyDataSetChanged()

                if (riwayatList.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Belum ada riwayat transaksi",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Terjadi kesalahan: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun formatTanggal(tanggal: String): String {
        return try {
            val input = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
            val output = SimpleDateFormat("EEE, d MMM yyyy", Locale("id", "ID"))
            output.format(input.parse(tanggal)!!)
        } catch (e: Exception) {
            tanggal
        }
    }

    private fun formatRupiah(nilai: Long): String {
        return "Rp " + String.format(Locale("id", "ID"), "%,d", nilai)
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            muatRiwayat()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}