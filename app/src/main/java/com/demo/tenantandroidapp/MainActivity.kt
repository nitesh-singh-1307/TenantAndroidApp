package com.demo.tenantandroidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var exit = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.textView)
        if (textView != null){
            println("TextView is initialized successfully!")
            exit = 0 // Exit with code 0 to indicate success
        }else{
            // new comment
            println("TextView is not initialized!")
            println("TextView is not initialized!")
            exit = 1 // Exit with code 1 to indicate failure
        }
    }
}