package com.example.knr_japota.Riwayat

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.knr_japota.databinding.ItemRiwayatBinding

class RiwayatAdapter(
    private val riwayatList: List<RiwayatModel>,
    private val onItemClick: (RiwayatModel) -> Unit
) : RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder>() {

    inner class RiwayatViewHolder(val binding: ItemRiwayatBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val binding = ItemRiwayatBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RiwayatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val item = riwayatList[position]
        with(holder.binding) {
            imgFoto.setImageResource(item.foto)
            tvLapangan.text = item.lapangan
            tvLokasi.text = item.lokasi
            tvJadwal.text = item.jadwal
            tvHarga.text = item.harga
            tvStatus.text = item.status

            // warna badge mengikuti status
            val warna = when (item.status) {
                "Aktif" -> "#1B1408"
                "Menunggu" -> "#FF6A1A"
                "Selesai" -> "#8C8C8C"
                else -> "#E53935"   // Ditolak
            }
            tvStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor(warna))

            root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun getItemCount(): Int = riwayatList.size
}