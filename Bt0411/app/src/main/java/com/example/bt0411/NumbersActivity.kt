package com.example.bt0411

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.bt0411.databinding.ActivityNumbersBinding
import kotlin.math.sqrt

class NumbersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNumbersBinding
    private lateinit var adapter: ArrayAdapter<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumbersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Bài 2: Danh sách số nguyên"

        // Adapter ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        binding.lvNumbers.adapter = adapter

        // Chỉ cho phép chọn 1 trong 6 radio (2 nhóm)
        makeMutuallyExclusive(binding.rgRow1, binding.rgRow2)

        // Sự kiện thay đổi
        binding.etLimit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateList()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val listener = RadioGroup.OnCheckedChangeListener { _, _ -> updateList() }
        binding.rgRow1.setOnCheckedChangeListener(listener)
        binding.rgRow2.setOnCheckedChangeListener(listener)

        // Khởi tạo
        updateList()
    }

    private fun makeMutuallyExclusive(groupA: RadioGroup, groupB: RadioGroup) {
        var lock = false
        val listenerA = RadioGroup.OnCheckedChangeListener { _, checkedId ->
            if (lock) return@OnCheckedChangeListener
            lock = true
            if (checkedId != -1) groupB.clearCheck()
            lock = false
        }
        val listenerB = RadioGroup.OnCheckedChangeListener { _, checkedId ->
            if (lock) return@OnCheckedChangeListener
            lock = true
            if (checkedId != -1) groupA.clearCheck()
            lock = false
        }
        groupA.setOnCheckedChangeListener(listenerA)
        groupB.setOnCheckedChangeListener(listenerB)
    }

    private fun parseLimit(): Int? {
        val s = binding.etLimit.text?.toString() ?: return null
        return s.toIntOrNull()
    }

    private fun updateList() {
        val n = parseLimit() ?: 0
        val list = when (selectedType()) {
            Type.ODD      -> oddsLessThan(n)
            Type.EVEN     -> evensLessThan(n)
            Type.PRIME    -> primesLessThan(n)
            Type.SQUARE   -> squaresLessThan(n)
            Type.PERFECT  -> perfectsLessThan(n)
            Type.FIBO     -> fiboLessThan(n)
        }

        adapter.clear()
        adapter.addAll(list)
        adapter.notifyDataSetChanged()

        binding.lvNumbers.isVisible = list.isNotEmpty()
        binding.tvEmpty.isVisible = list.isEmpty()
    }

    private enum class Type { ODD, EVEN, PRIME, SQUARE, PERFECT, FIBO }

    private fun selectedType(): Type {
        return when {
            binding.rbOdd.isChecked     -> Type.ODD
            binding.rbEven.isChecked    -> Type.EVEN
            binding.rbPrime.isChecked   -> Type.PRIME
            binding.rbSquare.isChecked  -> Type.SQUARE
            binding.rbPerfect.isChecked -> Type.PERFECT
            else                        -> Type.FIBO
        }
    }

    /*----------------- Bộ sinh các dãy số (< n) ------------------*/
    private fun oddsLessThan(n: Int)  = (1 until n step 2).toList()

    private fun evensLessThan(n: Int): List<Int> {
        if (n <= 2) return emptyList()
        return (2 until n step 2).toList()
    }

    private fun primesLessThan(n: Int): List<Int> {
        if (n <= 2) return emptyList()
        val isPrime = BooleanArray(n) { true }
        if (n > 0) isPrime[0] = false
        if (n > 1) isPrime[1] = false
        var p = 2
        while (p * p < n) {
            if (isPrime[p]) {
                var i = p * p
                while (i < n) {
                    isPrime[i] = false
                    i += p
                }
            }
            p++
        }
        val res = ArrayList<Int>()
        for (i in 2 until n) if (isPrime[i]) res.add(i)
        return res
    }

    private fun squaresLessThan(n: Int): List<Int> {
        val res = ArrayList<Int>()
        var k = 1
        while (k * k < n) {
            res.add(k * k)
            k++
        }
        return res
    }

    private fun perfectsLessThan(n: Int): List<Int> {
        val res = ArrayList<Int>()
        for (x in 2 until n) if (isPerfect(x)) res.add(x)
        return res
    }

    private fun isPerfect(x: Int): Boolean {
        if (x < 2) return false
        var sum = 1
        var i = 2
        val root = sqrt(x.toDouble()).toInt()
        while (i <= root) {
            if (x % i == 0) {
                sum += i
                val other = x / i
                if (other != i) sum += other
            }
            i++
        }
        return sum == x
    }

    private fun fiboLessThan(n: Int): List<Int> {
        val res = ArrayList<Int>()
        if (n <= 1) return res
        var a = 1
        var b = 1
        res.add(a)
        while (b < n) {
            if (res.last() != b) res.add(b) // tránh 1 bị lặp
            val c = a + b
            a = b
            b = c
            if (b >= n) break
        }
        return res
    }
}
