package com.example.expensetracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddIncomeActivity : AppCompatActivity() {

    private lateinit var incomeSourceEditText: EditText
    private lateinit var incomeAmountEditText: EditText
    private lateinit var incomeCategorySpinner: Spinner
    private lateinit var saveIncomeButton: Button
    private lateinit var database: DatabaseReference
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser: FirebaseUser? = mAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_income)

        incomeSourceEditText = findViewById(R.id.income_source)
        incomeAmountEditText = findViewById(R.id.income_amount)
        incomeCategorySpinner = findViewById(R.id.income_category_spinner)
        saveIncomeButton = findViewById(R.id.save_income_button)

        database = FirebaseDatabase.getInstance().reference

        // List of categories
        val categories = listOf("Salary", "Business", "Investment", "Gifts", "Other")

        // Setting up the Spinner with the categories
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        incomeCategorySpinner.adapter = adapter

        saveIncomeButton.setOnClickListener {
            saveIncome()
        }
    }

    private fun saveIncome() {
        val source = incomeSourceEditText.text.toString().trim()
        val amount = incomeAmountEditText.text.toString().trim()
        val category = incomeCategorySpinner.selectedItem.toString()

        if (source.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser?.uid
        val incomeId = database.child("users").child(userId ?: "").child("incomes").push().key
        val income = Income(incomeId, source, amount.toDouble(), category)

        if (incomeId != null && userId != null) {
            database.child("users").child(userId).child("incomes").child(incomeId).setValue(income).addOnCompleteListener {
                Toast.makeText(this, "Income saved", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}

data class Income(val id: String?, val source: String, val amount: Double, val category: String)
