package com.example.knr_japota

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PilihanAdapter(
    private val items: List<String>,
    private val layoutRes: Int,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<PilihanAdapter.ViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvLabel: TextView = view.findViewById(R.id.tvLabel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvLabel.text = items[position]
        // item terpilih memakai state_selected pada bg_pilihan.xml
        holder.tvLabel.isSelected = position == selectedPosition

        holder.itemView.setOnClickListener {
            val posBaru = holder.bindingAdapterPosition
            if (posBaru == RecyclerView.NO_POSITION) return@setOnClickListener

            val posLama = selectedPosition
            selectedPosition = posBaru
            if (posLama != RecyclerView.NO_POSITION) notifyItemChanged(posLama)
            notifyItemChanged(posBaru)

            onItemClick(items[posBaru])
        }
    }

    override fun getItemCount(): Int = items.size
}