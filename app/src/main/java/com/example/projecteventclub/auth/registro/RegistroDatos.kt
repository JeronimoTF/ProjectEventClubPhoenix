
package com.example.projecteventclub.auth.registro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projecteventclub.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class RegistroDatos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_datos)

        val etNombres: TextInputEditText = findViewById(R.id.etNombres)
        val etApellidos: TextInputEditText = findViewById(R.id.etApellidos)
        val etEdad: TextInputEditText = findViewById(R.id.etEdad)
        val spGenero: MaterialAutoCompleteTextView = findViewById(R.id.spGenero)
        val spTipoDocumento: MaterialAutoCompleteTextView = findViewById(R.id.spTipoDocumento)
        val etDocumento: TextInputEditText = findViewById(R.id.etDocumento)
        val etFechaNacimiento: TextInputEditText = findViewById(R.id.etFechaNacimiento)
        val btnSig1: Button = findViewById(R.id.btnSiguiente)

        btnSig1.setOnClickListener {
            val intent = Intent(this@RegistroDatos, RegistroLocalizacionUsu::class.java).apply {
                putExtra("nombres", etNombres.text.toString())
                putExtra("apellidos", etApellidos.text.toString())
                putExtra("edad", etEdad.text.toString())
                putExtra("genero", spGenero.text.toString())
                putExtra("tipoDocumento", spTipoDocumento.text.toString())
                putExtra("documento", etDocumento.text.toString())
                putExtra("fechaNacimiento", etFechaNacimiento.text.toString())
            }
            startActivity(intent)
        }
    }
}