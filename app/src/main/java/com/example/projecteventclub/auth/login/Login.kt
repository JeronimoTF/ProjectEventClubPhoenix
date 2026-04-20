package com.example.projecteventclub.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projecteventclub.MainActivity
import com.example.projecteventclub.R
import com.example.projecteventclub.auth.registro.RegistroDatos

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Botón ingresar como usuario

        val btn2: Button = findViewById(R.id.btnUsuario)

        btn2.setOnClickListener {
            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Botón ingresar como usuario

        val btn3: Button = findViewById(R.id.btnAnfitrion)

        btn3.setOnClickListener {
            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btn4: Button = findViewById(R.id.btnGoogle)

        btn4.setOnClickListener {
            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Dirigir a la pantalla de Registro

        val lnink = findViewById<TextView>(R.id.linkRegistro)

        lnink.setOnClickListener {
            val intent = Intent(this@Login, RegistroDatos::class.java)
            startActivity(intent)
        }
    }
}