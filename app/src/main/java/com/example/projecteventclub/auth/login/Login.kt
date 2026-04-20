package com.example.projecteventclub.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projecteventclub.MainActivity
import com.example.projecteventclub.R
import com.example.projecteventclub.vista_usuario.main.admin.activity_adminPrincipal
import com.example.projecteventclub.vista_usuario.main.usuarios.activity_usuarioPrincipal

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

        btn3.setOnClickListener {
            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}