package com.example.bt0411

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.bt0411.databinding.ActivityCurrencyBinding
import java.text.DecimalFormat
import kotlin.math.abs

class CurrencyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCurrencyBinding

    // Tỷ giá cố định: 1 đơn vị tiền tệ = ? USD
    private val toUSD = linkedMapOf(
        "USD" to 1.0,
        "EUR" to 1.07,
        "VND" to 0.000041, // 1 VND ~ 0.000041 USD
        "JPY" to 0.0067,
        "GBP" to 1.24,
        "AUD" to 0.66,
        "CAD" to 0.73,
        "SGD" to 0.73,
        "CNY" to 0.14,
        "KRW" to 0.00076,
        "THB" to 0.027,
        "INR" to 0.012
    )

    private val fmt = DecimalFormat("#,##0.######")
    private var suppress = false
    private var lastEdited: Edited = Edited.FROM

    private enum class Edited { FROM, TO }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrencyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Bài 1: Chuyển đổi tiền tệ"

        setupSpinners(binding.spFrom, binding.spTo)

        // Mặc định
        binding.spFrom.setSelection(0) // USD
        binding.spTo.setSelection(2)   // VND

        binding.etFrom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lastEdited = Edited.FROM
                recalc()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etTo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lastEdited = Edited.TO
                recalc()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val spinnerListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?, view: android.view.View?,
                position: Int, id: Long
            ) {
                recalc()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
        binding.spFrom.onItemSelectedListener = spinnerListener
        binding.spTo.onItemSelectedListener = spinnerListener
    }

    private fun setupSpinners(vararg spinners: Spinner) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, toUSD.keys.toList())
        spinners.forEach { it.adapter = adapter }
    }

    private fun parseDouble(s: String?): Double? {
        if (s.isNullOrBlank()) return null
        return s.replace(',', '.').toDoubleOrNull()
    }

    private fun recalc() {
        if (suppress) return
        suppress = true
        try {
            val fromCode = binding.spFrom.selectedItem.toString()
            val toCode = binding.spTo.selectedItem.toString()

            if (lastEdited == Edited.FROM) {
                val amount = parseDouble(binding.etFrom.text?.toString())
                if (amount == null) {
                    binding.etTo.setText("")
                } else {
                    val result = convert(amount, fromCode, toCode)
                    binding.etTo.setText(fmt.format(result))
                }
            } else {
                val amount = parseDouble(binding.etTo.text?.toString())
                if (amount == null) {
                    binding.etFrom.setText("")
                } else {
                    val result = convert(amount, toCode, fromCode)
                    binding.etFrom.setText(fmt.format(result))
                }
            }
        } finally {
            suppress = false
        }
    }

    private fun convert(amount: Double, from: String, to: String): Double {
        // amount[from] -> USD -> to
        val usd = amount * (toUSD[from] ?: 1.0)
        return usd / (toUSD[to] ?: 1.0)
    }
}
