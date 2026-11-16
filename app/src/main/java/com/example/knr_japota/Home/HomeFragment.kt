package com.example.knr_japota.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.knr_japota.Notifikasi.NotifikasiActivity
import com.example.knr_japota.Onboard.OnboardActivity
import com.example.knr_japota.R
import com.example.knr_japota.Sewa.SewaActivity
import com.example.knr_japota.databinding.DialogAturanBinding
import com.example.knr_japota.databinding.FragmentHomeBinding
import com.example.knr_japota.databinding.ItemAturanBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val daftarAturan = listOf(
        "Datang 10 menit sebelum jadwal main",
        "Wajib memakai sepatu olahraga non-marking",
        "Dilarang merokok di area lapangan",
        "Jaga kebersihan lapangan",
        "Pembatalan paling lambat 2 jam sebelum jadwal"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Home"
        }
        setHasOptionsMenu(true)

        binding.btnWebView.setOnClickListener {
            startActivity(Intent(requireContext(), WebViewActivity::class.java))
        }
        binding.btnPanduan.setOnClickListener {
            startActivity(Intent(requireContext(), OnboardActivity::class.java))
        }
        binding.btnAturan.setOnClickListener { tampilkanDialogAturan() }
        binding.btnSewa.setOnClickListener {
            startActivity(Intent(requireContext(), SewaActivity::class.java))
        }
    }

    /** Dialog Aturan: custom view dengan tema aplikasi */
    private fun tampilkanDialogAturan() {
        val dialogBinding = DialogAturanBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .create()
        // latar bawaan dialog dibuat transparan agar bg_dialog terlihat
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        for ((index, aturan) in daftarAturan.withIndex()) {
            val item = ItemAturanBinding.inflate(layoutInflater, dialogBinding.containerAturan, false)
            item.tvNomor.text = "${index + 1}"
            item.tvAturan.text = aturan
            dialogBinding.containerAturan.addView(item.root)
        }

        dialogBinding.btnTutupAturan.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notifikasi -> {
                startActivity(Intent(requireContext(), NotifikasiActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}