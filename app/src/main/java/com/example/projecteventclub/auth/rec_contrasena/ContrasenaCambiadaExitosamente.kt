package com.example.projecteventclub.auth.rec_contrasena

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projecteventclub.R
import com.example.projecteventclub.auth.login.Login

class ContrasenaCambiadaExitosamente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contrasena_cambiada_exitosamente)

        val btnSig1: Button = findViewById(R.id.btnVolverLogin)

        btnSig1.setOnClickListener {
            val intent = Intent(this@ContrasenaCambiadaExitosamente, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}