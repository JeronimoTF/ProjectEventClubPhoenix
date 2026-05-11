
package com.example.projecteventclub.auth.registro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projecteventclub.MainActivity
import com.example.projecteventclub.R
import com.example.projecteventclub.SupaBaseClient
import com.example.projecteventclub.models.UserProfile
import com.google.android.material.textfield.TextInputEditText
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class Contrasena : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contrasena)

        val etContra: TextInputEditText = findViewById(R.id.etContra)
        val etRecontra: TextInputEditText = findViewById(R.id.etRecontra)
        val checkTerminos: CheckBox = findViewById(R.id.CheckTerminos)
        val btnFinalizar: Button = findViewById(R.id.btnSiguiente3)

        btnFinalizar.setOnClickListener {
            val contra = etContra.text.toString()
            val recontra = etRecontra.text.toString()

            if (contra.isEmpty() || recontra.isEmpty()) {
                Toast.makeText(this, "Por favor, completa las contraseñas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contra != recontra) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!checkTerminos.isChecked) {
                Toast.makeText(this, "Debes aceptar los términos y condiciones", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registrarUsuarioEnSupabase(contra)
        }
    }

    private fun registrarUsuarioEnSupabase(password: String) {
        val email = intent.getStringExtra("correo") ?: ""

        if (email.isEmpty()) {
            Toast.makeText(this, "Error: No se encontró el correo electrónico", Toast.LENGTH_LONG).show()
            return
        }

        lifecycleScope.launch {
            try {
                // Registro en Supabase Auth
                SupaBaseClient.client.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }

                // Obtenemos el ID del usuario recién creado
                val userId = SupaBaseClient.client.auth.currentUserOrNull()?.id

                if (userId != null) {
                    // Preparamos el perfil para la base de datos
                    val perfil = UserProfile(
                        id = userId,
                        nombres = intent.getStringExtra("nombres") ?: "",
                        apellidos = intent.getStringExtra("apellidos") ?: "",
                        edad = intent.getStringExtra("edad") ?: "",
                        genero = intent.getStringExtra("genero") ?: "",
                        tipo_documento = intent.getStringExtra("tipoDocumento") ?: "",
                        documento = intent.getStringExtra("documento") ?: "",
                        fecha_nacimiento = intent.getStringExtra("fechaNacimiento") ?: "",
                        celular = intent.getStringExtra("celular") ?: "",
                        correo = email,
                        ciudad = intent.getStringExtra("ciudad") ?: "",
                        localidad = intent.getStringExtra("localidad") ?: "",
                        barrio = intent.getStringExtra("barrio") ?: "",
                        direccion = intent.getStringExtra("direccion") ?: ""
                    )

                    // Guardamos en la tabla 'perfiles'
                    SupaBaseClient.client.postgrest["perfiles"].insert(perfil)

                    Toast.makeText(this@Contrasena, "¡Registro completado con éxito!", Toast.LENGTH_SHORT).show()

                    // Al desactivar la confirmación, el usuario ya puede entrar directamente
                    val intent = Intent(this@Contrasena, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@Contrasena, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}