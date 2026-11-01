package com.example.knr_japota.Info

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.knr_japota.BookingActivity
import com.example.knr_japota.databinding.FragmentInfoBinding
import kotlin.jvm.java

class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Lapangan Tersedia"
        }

        binding.cardLapangan1.setOnClickListener { bukaBooking("Lapangan 1") }
        binding.cardLapangan2.setOnClickListener { bukaBooking("Lapangan 2") }
    }

    private fun bukaBooking(namaLapangan: String) {
        val intent = Intent(requireContext(), BookingActivity::class.java)
        intent.putExtra("namaLapangan", namaLapangan)
        startActivity(intent)
    }
}