package com.example.myapplication

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etBirthday: EditText? = null
    private var etAddress: EditText? = null
    private var etEmail: EditText? = null
    private var rgGender: RadioGroup? = null
    private var rbMale: RadioButton? = null
    private var rbFemale: RadioButton? = null
    private var cbAgree: CheckBox? = null
    private var btnSelect: Button? = null
    private var btnRegister: Button? = null

    private var selectedDate: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initViews()

        if (savedInstanceState != null) {
            etFirstName!!.setText(savedInstanceState.getString("firstName", ""))
            etLastName!!.setText(savedInstanceState.getString("lastName", ""))
            etBirthday!!.setText(savedInstanceState.getString("birthday", ""))
            etAddress!!.setText(savedInstanceState.getString("address", ""))
            etEmail!!.setText(savedInstanceState.getString("email", ""))
            val genderId = savedInstanceState.getInt("genderId", -1)
            if (genderId != -1) {
                rgGender!!.check(genderId)
            }
            cbAgree!!.isChecked = savedInstanceState.getBoolean("agreed", false)
            selectedDate = savedInstanceState.getString("selectedDate", "")
        }

        setupDatePicker()
        setupRegisterButton()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("firstName", etFirstName!!.text.toString())
        outState.putString("lastName", etLastName!!.text.toString())
        outState.putString("birthday", etBirthday!!.text.toString())
        outState.putString("address", etAddress!!.text.toString())
        outState.putString("email", etEmail!!.text.toString())
        outState.putInt("genderId", rgGender!!.checkedRadioButtonId)
        outState.putBoolean("agreed", cbAgree!!.isChecked)
        outState.putString("selectedDate", selectedDate)
    }

    private fun initViews() {
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etBirthday = findViewById(R.id.etBirthday)
        etAddress = findViewById(R.id.etAddress)
        etEmail = findViewById(R.id.etEmail)
        rgGender = findViewById(R.id.rgGender)
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
        cbAgree = findViewById(R.id.cbAgree)
        btnSelect = findViewById(R.id.btnSelect)
        btnRegister = findViewById(R.id.btnRegister)
    }

    private fun setupDatePicker() {
        btnSelect!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this@RegisterActivity,
                { _, y, m, d ->
                    selectedDate = "$d/${m + 1}/$y"
                    etBirthday!!.setText(selectedDate)
                },
                year, month, day
            )
            datePickerDialog.show()
        }
    }

    private fun setupRegisterButton() {
        btnRegister!!.setOnClickListener {
            if (validateForm()) {
                registerUser()
            }
        }
    }

    private fun validateForm(): Boolean {
        val firstName = etFirstName!!.text.toString().trim()
        val lastName = etLastName!!.text.toString().trim()
        val birthday = etBirthday!!.text.toString().trim()
        val address = etAddress!!.text.toString().trim()
        val email = etEmail!!.text.toString().trim()
        val selectedGenderId = rgGender!!.checkedRadioButtonId

        if (firstName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập First Name", Toast.LENGTH_SHORT).show()
            etFirstName!!.requestFocus()
            return false
        }

        if (lastName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Last Name", Toast.LENGTH_SHORT).show()
            etLastName!!.requestFocus()
            return false
        }

        if (selectedGenderId == -1) {
            Toast.makeText(this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show()
            return false
        }

        if (birthday.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày sinh", Toast.LENGTH_SHORT).show()
            return false
        }

        if (address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show()
            etAddress!!.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show()
            etEmail!!.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
            etEmail!!.requestFocus()
            return false
        }

        if (!cbAgree!!.isChecked) {
            Toast.makeText(this, "Vui lòng đồng ý với Terms of Use", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun registerUser() {
        val firstName = etFirstName!!.text.toString().trim()
        val lastName = etLastName!!.text.toString().trim()
        val birthday = etBirthday!!.text.toString().trim()
        val address = etAddress!!.text.toString().trim()
        val email = etEmail!!.text.toString().trim()
        val selectedGenderId = rgGender!!.checkedRadioButtonId
        val selectedGender = findViewById<RadioButton>(selectedGenderId)
        val gender = selectedGender.text.toString()
        val message = "Đăng ký thành công!\n" +
                "Họ tên: $firstName $lastName\n" +
                "Giới tính: $gender\n" +
                "Ngày sinh: $birthday\n" +
                "Địa chỉ: $address\n" +
                "Email: $email"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        val rootView = findViewById<View>(android.R.id.content)
        rootView.setBackgroundColor(getColor(android.R.color.holo_red_light))
    }
}
