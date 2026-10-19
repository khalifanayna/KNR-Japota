package com.example.knr_japota

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.knr_japota.Home.HomeFragment
import com.example.knr_japota.Info.InfoFragment
import com.example.knr_japota.Profile.ProfileFragment
import com.example.knr_japota.databinding.ActivityBaseBinding
import com.example.knr_japota.databinding.ActivityMainBinding

class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        /** HomeFragment sebagai fragment default */
        replaceFragment(HomeFragment())
        setNavColor("#A97C21")

        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    setNavColor("#A97C21")
                    true
                }
                R.id.info -> {
                    replaceFragment(InfoFragment())
                    setNavColor("#FFC93C")
                    true
                }
                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                    setNavColor("#FFC93C")
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            //.addToBackStack(null) -> ini kita nonaktifkan agar saat back langsung keluar aplikasi
            .commit()
    }

    private fun setNavColor(hex: String) {
        binding.bottomNavView.setBackgroundColor(Color.parseColor(hex))
    }
}