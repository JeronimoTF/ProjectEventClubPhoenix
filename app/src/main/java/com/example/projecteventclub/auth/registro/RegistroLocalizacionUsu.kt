package com.example.projecteventclub.auth.registro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

        // Capturar los campos del XML
        val etCelular: TextInputEditText = findViewById(R.id.etCelular)
        val etCorreo: TextInputEditText = findViewById(R.id.etCorreo)
        val etCiudad: TextInputEditText = findViewById(R.id.etCiudadResi)
        val spLocalidad: MaterialAutoCompleteTextView = findViewById(R.id.spLocalidad)
        val spBarrio: MaterialAutoCompleteTextView = findViewById(R.id.spBarrio)
        val etDireccion: TextInputEditText = findViewById(R.id.etDireccion)
        val btnSig2: Button = findViewById(R.id.btnSiguiente2)

        btnSig2.setOnClickListener {
            // Pasamos TODO a la siguiente pantalla (Contrasena)
            val intent = Intent(this@RegistroLocalizacionUsu, Contrasena::class.java).apply {
                // Datos que vienen de la pantalla anterior (RegistroDatos)
                putExtra("nombres", intent.getStringExtra("nombres"))
                putExtra("apellidos", intent.getStringExtra("apellidos"))
                putExtra("edad", intent.getStringExtra("edad"))
                putExtra("genero", intent.getStringExtra("genero"))
                putExtra("tipoDocumento", intent.getStringExtra("tipoDocumento"))
                putExtra("documento", intent.getStringExtra("documento"))
                putExtra("fechaNacimiento", intent.getStringExtra("fechaNacimiento"))

                // Datos de ESTA pantalla
                putExtra("celular", etCelular.text.toString())
                putExtra("correo", etCorreo.text.toString())
                putExtra("ciudad", etCiudad.text.toString())
                putExtra("localidad", spLocalidad.text.toString())
                putExtra("barrio", spBarrio.text.toString())
                putExtra("direccion", etDireccion.text.toString())
            }
            startActivity(intent)
        }
    }
}
