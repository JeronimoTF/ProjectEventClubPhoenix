package com.example.projecteventclub.auth.rec_contraseña

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projecteventclub.R
import com.example.projecteventclub.auth.registro.Confirmacion

class RecuperarContrasena : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_contrasena)

        val btnSig1: Button = findViewById(R.id.btnRecuperaContra1)

        btnSig1.setOnClickListener {
            val intent = Intent(this@RecuperarContrasena, CodigoVerificacion::class.java)
            startActivity(intent)
            finish()
        }
    }
}