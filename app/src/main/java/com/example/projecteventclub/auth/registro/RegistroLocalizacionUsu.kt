package com.example.projecteventclub.auth.registro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projecteventclub.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class RegistroLocalizacionUsu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_localizacion_usu)

        val etCelular: TextInputEditText = findViewById(R.id.etCelular)
        val etCorreo: TextInputEditText = findViewById(R.id.etCorreo)
        val etCiudad: TextInputEditText = findViewById(R.id.etCiudadResi)
        val spLocalidad: MaterialAutoCompleteTextView = findViewById(R.id.spLocalidad)
        val spBarrio: MaterialAutoCompleteTextView = findViewById(R.id.spBarrio)
        val etDireccion: TextInputEditText = findViewById(R.id.etDireccion)
        val btnSig2: Button = findViewById(R.id.btnSiguiente2)

        btnSig2.setOnClickListener {
            val celular = etCelular.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val ciudad = etCiudad.text.toString().trim()
            val localidad = spLocalidad.text.toString().trim()
            val barrio = spBarrio.text.toString().trim()
            val direccion = etDireccion.text.toString().trim()

            // VALIDACIONES
            if (celular.isEmpty()) {
                etCelular.error = "El celular es obligatorio"
                etCelular.requestFocus()
                return@setOnClickListener
            }
            if (correo.isEmpty()) {
                etCorreo.error = "El correo es obligatorio"
                etCorreo.requestFocus()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                etCorreo.error = "Ingrese un correo válido"
                etCorreo.requestFocus()
                return@setOnClickListener
            }
            if (ciudad.isEmpty()) {
                etCiudad.error = "La ciudad es obligatoria"
                etCiudad.requestFocus()
                return@setOnClickListener
            }
            if (localidad.isEmpty()) {
                Toast.makeText(this, "Seleccione una localidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (barrio.isEmpty()) {
                Toast.makeText(this, "Seleccione un barrio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (direccion.isEmpty()) {
                etDireccion.error = "La dirección es obligatoria"
                etDireccion.requestFocus()
                return@setOnClickListener
            }

            // Si pasa las validaciones, enviamos todo a la pantalla de Contraseña
            val intentNext = Intent(this@RegistroLocalizacionUsu, Contrasena::class.java).apply {
                // Datos de la pantalla anterior
                putExtra("nombres", intent.getStringExtra("nombres"))
                putExtra("apellidos", intent.getStringExtra("apellidos"))
                putExtra("edad", intent.getStringExtra("edad"))
                putExtra("genero", intent.getStringExtra("genero"))
                putExtra("tipoDocumento", intent.getStringExtra("tipoDocumento"))
                putExtra("documento", intent.getStringExtra("documento"))
                putExtra("fechaNacimiento", intent.getStringExtra("fechaNacimiento"))

                // Datos de esta pantalla
                putExtra("celular", celular)
                putExtra("correo", correo)
                putExtra("ciudad", ciudad)
                putExtra("localidad", localidad)
                putExtra("barrio", barrio)
                putExtra("direccion", direccion)
            }
            startActivity(intentNext)
        }
    }
}
