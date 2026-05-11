package com.example.projecteventclub.auth.rec_contrasena

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projecteventclub.R
import com.example.projecteventclub.SupaBaseClient
import com.example.projecteventclub.auth.login.Login
import com.google.android.material.textfield.TextInputEditText
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class NuevaContrasena : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nueva_contrasena)

        val etContra: TextInputEditText = findViewById(R.id.etContra)
        val etRecontra: TextInputEditText = findViewById(R.id.etRecontra)
        val btnCambiar: Button = findViewById(R.id.btnRecuperaContra3)

        btnCambiar.setOnClickListener {
            val nuevaContra = etContra.text.toString()
            val confirmarContra = etRecontra.text.toString()

            if (nuevaContra.isEmpty() || confirmarContra.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nuevaContra != confirmarContra) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mínimo de caracteres (opcional, Supabase suele pedir 6)
            if (nuevaContra.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            actualizarContrasena(nuevaContra)
        }
    }

    private fun actualizarContrasena(nuevaContra: String) {
        lifecycleScope.launch {
            try {
                // Al haber verificado el código en la pantalla anterior, 
                // ya tenemos una sesión activa para este cambio.
                SupaBaseClient.client.auth.updateUser {
                    password = nuevaContra
                }

                Toast.makeText(this@NuevaContrasena, "Contraseña actualizada correctamente", Toast.LENGTH_LONG).show()

                // Redirigir al Login para que entre con su nueva contraseña
                val intent = Intent(this@NuevaContrasena, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@NuevaContrasena, "Error al actualizar: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
