package com.example.projecteventclub.auth.rec_contraseña

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projecteventclub.R

class CodigoVerificacion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_codigo_verificacion)

        val btnSig1: Button = findViewById(R.id.btnREcuperaContra2)

        btnSig1.setOnClickListener {
            val intent = Intent(this@CodigoVerificacion, NuevaContrasena::class.java)
            startActivity(intent)
            finish()
        }
    }
}