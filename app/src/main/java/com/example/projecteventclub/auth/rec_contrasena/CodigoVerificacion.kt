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
import com.google.android.material.textfield.TextInputEditText
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.OtpType
import kotlinx.coroutines.launch

class CodigoVerificacion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_codigo_verificacion)

        val etCodigo: TextInputEditText = findViewById(R.id.etCodigoConfir)
        val btnVerificar: Button = findViewById(R.id.btnREcuperaContra2)
        val email = intent.getStringExtra("email") ?: ""

        btnVerificar.setOnClickListener {
            val codigo = etCodigo.text.toString().trim()
            if (codigo.isNotEmpty() && email.isNotEmpty()) {
                verificarCodigo(email, codigo)
            } else {
                Toast.makeText(this, "Ingresa el código", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verificarCodigo(email: String, codigo: String) {
        lifecycleScope.launch {
            try {
                // En Supabase Kotlin 3.x la función es verifyEmailOtp
                SupaBaseClient.client.auth.verifyEmailOtp(
                    type = OtpType.Email.RECOVERY,
                    email = email,
                    token = codigo
                )

                Toast.makeText(this@CodigoVerificacion, "Código verificado", Toast.LENGTH_SHORT).show()

                // Si es correcto, vamos a la pantalla para poner la nueva contraseña
                val intent = Intent(this@CodigoVerificacion, NuevaContrasena::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@CodigoVerificacion, "Error: Código inválido o expirado", Toast.LENGTH_LONG).show()
            }
        }
    }
}
