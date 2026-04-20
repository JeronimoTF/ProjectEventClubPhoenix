package com.example.projecteventclub.auth.rec_contraseña

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projecteventclub.R
import com.example.projecteventclub.auth.login.Login
import com.example.projecteventclub.auth.registro.Contrasena

class ErrorCambioContrasena : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_error_cambio_contrasena)

        val btnSig1: Button = findViewById(R.id.btnVolverRecuperarContra)

        btnSig1.setOnClickListener {
            val intent = Intent(this@ErrorCambioContrasena, RecuperarContrasena::class.java)
            startActivity(intent)
            finish()
        }
    }
}