package com.demo.tenantandroidapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

class RegistrationActivity : AppCompatActivity() {
    private lateinit var edt_emailId : EditText
    private lateinit var edt_password : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        val email = "user@example.com"
        val password = "password123"
        val str_submit = findViewById<Button>(R.id.btbSubmit)
        edt_emailId = findViewById<EditText>(R.id.editEmail)
        edt_password = findViewById<EditText>(R.id.editPassword)
        edt_emailId.setText("user5@example.com")
        edt_password.setText("password123")
        val email1: String = edt_emailId.getText().toString().trim()
        val password1: String = edt_password.getText().toString().trim()

        str_submit.setOnClickListener {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email1, password1)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Registration successful
                        val user = FirebaseAuth.getInstance().currentUser
                        // Handle user registration success
                        val riderId = user?.uid ?: ""
                        val riderName = "John Doe"
                        val riderPhone = "+123456789"
                        val riderAddress = "uma char rasta, vadodara, Gujarat, 394210"
                        // Step 5: Store Rider Details in the Realtime Database
                        val database = FirebaseDatabase.getInstance()
                        val ridersRef = database.getReference("riders")
                        val riderRef = ridersRef.child(riderId)

                        val rider = Rider(riderId, riderName, riderPhone,riderAddress,Location(22.301835,73.2301593))
                        riderRef.setValue(rider)
                        Log.d("TAG","checkUser:::::::::$user")
                        val intent = Intent(this,LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Registration failed
                        val exception = task.exception
                        Log.d("TAG","checkUser:::::::::$exception")
                        Toast.makeText(this, "Registration failed: ${exception?.message}", Toast.LENGTH_SHORT).show()

                        // Handle user registration failure
                    }
                }
        }

    }
}