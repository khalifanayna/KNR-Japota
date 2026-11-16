package com.example.knr_japota.Notifikasi

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.knr_japota.databinding.ItemNotifikasiBinding
import com.google.android.material.snackbar.Snackbar

class NotifikasiAdapter(
    context: Context,
    private val notifikasi: List<NotifikasiModel>
) : ArrayAdapter<NotifikasiModel>(context, 0, notifikasi) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemNotifikasiBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = binding.root

        val data = notifikasi[position]
        binding.tvJudul.text = data.judul
        binding.tvKeterangan.text = data.keterangan

        // warna ikon mengikuti status
        val warna = when (data.kategori) {
            "Disetujui" -> "#4CAF50"
            "Menunggu" -> "#FF6A1A"
            "Selesai" -> "#FFC93C"
            else -> "#E53935"   // Ditolak
        }
        binding.imgIkon.setColorFilter(Color.parseColor(warna))

        // seperti di modul: event click ditulis di adapter
        view.setOnClickListener {
            Snackbar.make(parent, "${data.judul}: ${data.keterangan}", Snackbar.LENGTH_SHORT).show()
        }

        return view
    }
}