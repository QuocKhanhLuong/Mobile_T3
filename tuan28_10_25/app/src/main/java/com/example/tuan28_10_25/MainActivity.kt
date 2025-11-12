package com.example.tuan28_10_25

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var tvDisplay: TextView? = null
    private var currentNumber = ""
    private var operator = ""
    private var firstNumber = 0.0
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.tvDisplay)

        if (savedInstanceState != null) {
            currentNumber = savedInstanceState.getString("currentNumber", "")
            operator = savedInstanceState.getString("operator", "")
            firstNumber = savedInstanceState.getDouble("firstNumber", 0.0)
            isNewOperation = savedInstanceState.getBoolean("isNewOperation", true)
            tvDisplay!!.text = if (currentNumber.isEmpty()) "0" else currentNumber
        }

        setupNumberButtons()
        setupOperatorButtons()
        setupFunctionButtons()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentNumber", currentNumber)
        outState.putString("operator", operator)
        outState.putDouble("firstNumber", firstNumber)
        outState.putBoolean("isNewOperation", isNewOperation)
    }

    private fun setupNumberButtons() {
        val numberIds = intArrayOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        val numberListener = View.OnClickListener { v ->
            val button = v as Button
            val number = button.text.toString()

            currentNumber = if (isNewOperation) {
                isNewOperation = false
                number
            } else {
                currentNumber + number
            }
            tvDisplay!!.text = currentNumber
        }

        for (id in numberIds) findViewById<View>(id).setOnClickListener(numberListener)

        findViewById<View>(R.id.btnDot).setOnClickListener {
            if (!currentNumber.contains(".")) {
                currentNumber = if (currentNumber.isEmpty()) "0." else "$currentNumber."
                tvDisplay!!.text = currentNumber
            }
        }
    }

    private fun setupOperatorButtons() {
        val operatorListener = View.OnClickListener { v ->
            val button = v as Button
            val op = button.text.toString()

            if (currentNumber.isNotEmpty()) {
                if (operator.isNotEmpty()) {
                    calculateResult()
                } else {
                    firstNumber = currentNumber.toDouble()
                }
                operator = op
                isNewOperation = true
            }
        }

        findViewById<View>(R.id.btnAdd).setOnClickListener(operatorListener)
        findViewById<View>(R.id.btnSubtract).setOnClickListener(operatorListener)
        findViewById<View>(R.id.btnMultiply).setOnClickListener(operatorListener)
        findViewById<View>(R.id.btnDivide).setOnClickListener(operatorListener)
    }

    private fun setupFunctionButtons() {
        findViewById<View>(R.id.btnC).setOnClickListener {
            currentNumber = ""
            tvDisplay!!.text = "0"
        }

        findViewById<View>(R.id.btnCE).setOnClickListener {
            currentNumber = ""
            operator = ""
            firstNumber = 0.0
            isNewOperation = true
            tvDisplay!!.text = "0"
        }

        findViewById<View>(R.id.btnBS).setOnClickListener {
            if (currentNumber.isNotEmpty()) {
                currentNumber = currentNumber.dropLast(1)
                tvDisplay!!.text = if (currentNumber.isEmpty()) "0" else currentNumber
            }
        }

        findViewById<View>(R.id.btnPlusMinus).setOnClickListener {
            if (currentNumber.isNotEmpty() && currentNumber != "0") {
                currentNumber = if (currentNumber.startsWith("-")) {
                    currentNumber.substring(1)
                } else {
                    "-$currentNumber"
                }
                tvDisplay!!.text = currentNumber
            }
        }

        findViewById<View>(R.id.btnEquals).setOnClickListener {
            calculateResult()
        }
    }

    private fun calculateResult() {
        if (currentNumber.isNotEmpty() && operator.isNotEmpty()) {
            val secondNumber = currentNumber.toDouble()
            var result = 0.0

            when (operator) {
                "+" -> result = firstNumber + secondNumber
                "-" -> result = firstNumber - secondNumber
                "x" -> result = firstNumber * secondNumber
                "/" -> if (secondNumber != 0.0) {
                    result = firstNumber / secondNumber
                } else {
                    tvDisplay!!.text = "Error"
                    reset()
                    return
                }
            }

            currentNumber = if (result == result.toLong().toDouble()) {
                result.toLong().toString()
            } else {
                result.toString()
            }

            tvDisplay!!.text = currentNumber
            firstNumber = result
            operator = ""
            isNewOperation = true
        }
    }

    private fun reset() {
        currentNumber = ""
        operator = ""
        firstNumber = 0.0
        isNewOperation = true
    }
}
