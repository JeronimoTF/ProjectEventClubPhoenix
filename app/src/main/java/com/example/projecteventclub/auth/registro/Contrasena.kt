package com.example.projecteventclub.auth.registro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projecteventclub.R

class Contrasena : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contrasena)

        val btnSig1: Button = findViewById(R.id.btnSiguiente3)

        btnSig1.setOnClickListener {
            val intent = Intent(this@Contrasena, Confirmacion::class.java)
            startActivity(intent)
            finish()
        }
    }
}