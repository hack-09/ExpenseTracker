package com.example.expensetracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserProfileActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var profileName: TextView
    private lateinit var profileInitialTextView: TextView
    private lateinit var signInButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        profileImage = findViewById(R.id.profile_image)
        profileName = findViewById(R.id.profile_name)
        signInButton = findViewById(R.id.sign_in_button)
        profileInitialTextView = findViewById(R.id.profile_initial)

        auth = FirebaseAuth.getInstance()

        signInButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        // Update UI if user is already signed in
        updateUI(auth.currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            profileName.text = user.displayName ?: user.email ?: "User Name"
            val initial = profileName.text.first().toString().toUpperCase()
            // Load user's profile image if available
            // profileImage.setImageURI(user.photoUrl)
            profileInitialTextView.visibility = View.VISIBLE
            profileInitialTextView.text = initial
            signInButton.text = "Sign Out"
            signInButton.setOnClickListener {
                auth.signOut()
                updateUI(null)
            }
        } else {
            profileName.text = "User Name"
            profileImage.setImageResource(R.drawable.userprofile) // Default profile icon
            signInButton.text = "Sign In"
            signInButton.setOnClickListener {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
