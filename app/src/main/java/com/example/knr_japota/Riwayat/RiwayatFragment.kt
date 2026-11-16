package com.example.knr_japota.Riwayat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.knr_japota.R
import com.example.knr_japota.databinding.FragmentRiwayatBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RiwayatFragment : Fragment() {
    private var _binding: FragmentRiwayatBinding? = null
    private val binding get() = _binding!!

    private val riwayatList = listOf(
        RiwayatModel("Lapangan 1", "GOR Gabus Rumbai", "Sel, 17 Nov 2026 | 19:00 - 20:00", "Rp 35.000", "Aktif", R.drawable.lapangangor),
        RiwayatModel("Lapangan 2", "GOR Gabus Rumbai", "Kam, 19 Nov 2026 | 15:00 - 16:00", "Rp 35.000", "Menunggu", R.drawable.lapangangor),
        RiwayatModel("Lapangan 2", "GOR Gabus Rumbai", "Sab, 12 Sep 2026 | 15:00 - 16:00", "Rp 35.000", "Selesai", R.drawable.lapangangor),
        RiwayatModel("Lapangan 1", "GOR Gabus Rumbai", "Min, 11 Jan 2026 | 21:00 - 22:00", "Rp 35.000", "Ditolak", R.drawable.lapangangor)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Riwayat Booking"
        }

        val adapter = RiwayatAdapter(riwayatList) { item ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Detail Pesanan")
                .setMessage("${item.lapangan}\n${item.lokasi}\n${item.jadwal}\nTotal: ${item.harga}\nStatus: ${item.status}")
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .show()
        }

        binding.rvRiwayat.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}