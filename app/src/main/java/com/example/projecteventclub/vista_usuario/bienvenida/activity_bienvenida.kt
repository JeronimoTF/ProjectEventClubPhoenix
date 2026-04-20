package com.example.projecteventclub.vista_usuario.bienvenida

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projecteventclub.R
import com.example.projecteventclub.auth.login.Login
import com.google.android.material.button.MaterialButton
import kotlin.jvm.java


class activity_bienvenida : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bienvenida)

        // Botón comenzar

        val btn: Button = findViewById(R.id.btnComenzar)

        btn.setOnClickListener {
            val intent = Intent(this@activity_bienvenida, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}