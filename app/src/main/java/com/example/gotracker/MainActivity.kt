package com.example.gotracker

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  Add 2-second delay before checking login
        Handler(Looper.getMainLooper()).postDelayed({

            val user = auth.currentUser
            if (user != null) {
                dbRef.child(user.uid).get().addOnSuccessListener { snapshot ->
                    val role = snapshot.child("role").value.toString()
                    when (role) {
                        "admin" -> startActivity(Intent(this, AdminDashboardActivity::class.java))
                       // "passenger"-> startActivity(Intent(this,PassengerDashboardActivity::class.java))
                    }
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to load role", Toast.LENGTH_SHORT).show()
                }
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()

            }

        }, 2000) // 2000 milliseconds = 2 seconds
    }
}