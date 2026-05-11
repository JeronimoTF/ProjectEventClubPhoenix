package com.example.projecteventclub.auth.rec_contrasena

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projecteventclub.R
import com.example.projecteventclub.SupaBaseClient
import com.google.android.material.textfield.TextInputEditText
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class RecuperarContrasena : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_contrasena)

        val etEmail: TextInputEditText = findViewById(R.id.etContra)
        val btnEnviar: Button = findViewById(R.id.btnRecuperaContra1)

        btnEnviar.setOnClickListener {
            val email = etEmail.text.toString().trim().lowercase()

            if (email.isNotEmpty()) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    enviarCorreoRecuperacion(email)
                } else {
                    Toast.makeText(this, "Formato de correo inválido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, ingresa tu correo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enviarCorreoRecuperacion(email: String) {
        lifecycleScope.launch {
            try {
                Log.d("SupabaseAuth", "Solicitando recuperación para: $email")

                // En Supabase 3.x, resetPasswordForEmail inicia el flujo de recuperación
                SupaBaseClient.client.auth.resetPasswordForEmail(email)

                Log.d("SupabaseAuth", "Petición aceptada por Supabase")
                Toast.makeText(this@RecuperarContrasena, "Petición enviada. Revisa tu correo.", Toast.LENGTH_LONG).show()

                val intent = Intent(this@RecuperarContrasena, CodigoVerificacion::class.java)
                intent.putExtra("email", email)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Log.e("SupabaseAuth", "Error al conectar: ${e.message}")
                Toast.makeText(this@RecuperarContrasena, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
