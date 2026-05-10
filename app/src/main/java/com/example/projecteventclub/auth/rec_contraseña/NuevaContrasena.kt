package com.example.projecteventclub.auth.rec_contraseña

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projecteventclub.R
import com.example.projecteventclub.auth.registro.RegistroDatos

class NuevaContrasena : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nueva_contrasena)

        val btnSig1: Button = findViewById(R.id.btnRecuperaContra3)

        btnSig1.setOnClickListener {
            val intent = Intent(this@NuevaContrasena, ContrasenaCambiadaExitosamente::class.java)
            startActivity(intent)
            finish()
        }

        val lnink = findViewById<TextView >(R.id.DescripcionRecCuenta_CorreoElectro)

        lnink.setOnClickListener {
            val intent = Intent(this@NuevaContrasena, ErrorCambioContrasena::class.java)
            startActivity(intent)
        }
    }
}