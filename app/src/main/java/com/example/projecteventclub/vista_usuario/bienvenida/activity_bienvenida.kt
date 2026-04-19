package com.example.projecteventclub.vista_usuario.bienvenida

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projecteventclub.R
import com.example.projecteventclub.vista_usuario.main.admin.activity_main_admin

class activity_bienvenida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bienvenida)

        // Pasar a la pantalla de admin automáticamente después de 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, activity_main_admin::class.java)
            startActivity(intent)
            finish() // Cerramos bienvenida para que no pueda regresar con el botón atrás
        }, 3000)
    }
}