package com.example.expensetracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var expenseNameEditText: EditText
    private lateinit var expenseAmountEditText: EditText
    private lateinit var expenseCategorySpinner: Spinner
    private lateinit var saveExpenseButton: Button
    private lateinit var database: DatabaseReference
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser: FirebaseUser? = mAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        expenseNameEditText = findViewById(R.id.expense_name)
        expenseAmountEditText = findViewById(R.id.expense_amount)
        expenseCategorySpinner = findViewById(R.id.expense_category_spinner)
        saveExpenseButton = findViewById(R.id.save_expense_button)

        database = FirebaseDatabase.getInstance().reference

        // List of categories
        val categories = listOf("Rent/Mortgage", "Utilities", "Groceries", "Transportation", "Insurance", "Healthcare", "Debt Payments", "Savings", "Entertainment", "Dining Out", "Personal Care", "Education", "Gifts/Donations", "Miscellaneous")

        // Setting up the Spinner with the categories
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        expenseCategorySpinner.adapter = adapter

        saveExpenseButton.setOnClickListener {
            saveExpense()
        }
    }

    private fun saveExpense() {
        val name = expenseNameEditText.text.toString().trim()
        val amount = expenseAmountEditText.text.toString().trim()
        val category = expenseCategorySpinner.selectedItem.toString()

        if (name.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser?.uid
        val expenseId = database.child(userId ?: "").push().key
        val expense = Expense(expenseId, name, amount.toDouble(), category)

        if (expenseId != null && userId != null) {
            database.child("users").child(userId).child("expenses").child(expenseId).setValue(expense).addOnCompleteListener {
                Toast.makeText(this, "Expense saved", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}

data class Expense(val id: String?, val name: String, val amount: Double, val category: String)
