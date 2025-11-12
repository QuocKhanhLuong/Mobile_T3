package com.example.bt0411

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bt0411.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBai1.setOnClickListener {
            startActivity(Intent(this, CurrencyActivity::class.java))
        }
        binding.btnBai2.setOnClickListener {
            startActivity(Intent(this, NumbersActivity::class.java))
        }
    }
}
