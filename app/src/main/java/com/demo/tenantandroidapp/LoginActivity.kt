package com.demo.tenantandroidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var edt_emailId : EditText
    private lateinit var edt_password : EditText
    private lateinit var btbSubmitLogin : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        val email = "user@example.com"
//        val password = "password123"

        btbSubmitLogin = findViewById<Button>(R.id.btbSubmitLogin)
        edt_emailId = findViewById<EditText>(R.id.editEmailLogin)
        edt_password = findViewById<EditText>(R.id.editPasswordLogin)
        edt_emailId.setText("user2@example.com")
        edt_password.setText("password123")

        val email1: String = edt_emailId.getText().toString().trim()
        val password1: String = edt_password.getText().toString().trim()
//        val btbSubmitLogin: String = edt_password.getText().toString().trim()
        btbSubmitLogin.setOnClickListener {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email1, password1)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Login successful
                        val user = FirebaseAuth.getInstance().currentUser
                        // Handle user login success
                        Log.d("TAG","loginSuccess::::$user")
                    } else {
                        // Login failed
                        val exception = task.exception
                        // Handle user login failure
                    }
                }
        }

    }

    val auth = FirebaseAuth.getInstance()
    val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        if (user != null) {
            // User is signed in
            // Handle user signed-in state
        } else {
            // User is signed out
            // Handle user signed-out state
        }
    }

// Register the AuthStateListener
//    auth.addAuthStateListener(authStateListener)
}