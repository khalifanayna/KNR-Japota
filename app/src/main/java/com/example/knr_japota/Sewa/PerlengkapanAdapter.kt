
package com.example.knr_japota.Sewa

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.knr_japota.databinding.ItemPerlengkapanBinding

class PerlengkapanAdapter(
    private var items: List<PerlengkapanModel>,
    private val onChanged: () -> Unit
) : RecyclerView.Adapter<PerlengkapanAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPerlengkapanBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPerlengkapanBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        with(holder.binding) {
            imgIkon.setImageResource(item.ikon)
            imgIkon.contentDescription = item.nama

            tvNama.text = item.nama
            tvHarga.text = formatRupiah(item.harga)

            root.isSelected = item.dipilih

            root.setOnClickListener {
                val posisi = holder.bindingAdapterPosition

                if (posisi != RecyclerView.NO_POSITION) {
                    item.dipilih = !item.dipilih
                    notifyItemChanged(posisi)
                    onChanged()
                }
            }
        }
    }

    /**
     * Dipakai saat filter Chip berubah
     */
    fun updateData(dataBaru: List<PerlengkapanModel>) {
        items = dataBaru
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size
}
