package com.example.projecteventclub.auth.registro

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projecteventclub.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.util.Calendar

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

        // Configuración del Selector de Fecha
        etFechaNacimiento.setOnClickListener {
            showDatePickerDialog(etFechaNacimiento)
        }

        btnSig1.setOnClickListener {
            val nombres = etNombres.text.toString().trim()
            val apellidos = etApellidos.text.toString().trim()
            val edad = etEdad.text.toString().trim()
            val genero = spGenero.text.toString().trim()
            val tipoDoc = spTipoDocumento.text.toString().trim()
            val documento = etDocumento.text.toString().trim()
            val fechaNac = etFechaNacimiento.text.toString().trim()

            // VALIDACIÓN DE CAMPOS
            if (nombres.isEmpty()) {
                etNombres.error = "El nombre es obligatorio"
                etNombres.requestFocus()
                return@setOnClickListener
            }
            if (apellidos.isEmpty()) {
                etApellidos.error = "El apellido es obligatorio"
                etApellidos.requestFocus()
                return@setOnClickListener
            }
            if (edad.isEmpty()) {
                etEdad.error = "La edad es obligatoria"
                etEdad.requestFocus()
                return@setOnClickListener
            }
            if (genero.isEmpty()) {
                Toast.makeText(this, "Por favor seleccione un género", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (tipoDoc.isEmpty()) {
                Toast.makeText(this, "Seleccione tipo de documento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (documento.isEmpty()) {
                etDocumento.error = "El documento es obligatorio"
                etDocumento.requestFocus()
                return@setOnClickListener
            }
            if (fechaNac.isEmpty()) {
                etFechaNacimiento.error = "La fecha es obligatoria"
                return@setOnClickListener
            }

            // Si todo está bien, pasamos a la siguiente pantalla
            val intent = Intent(this@RegistroDatos, RegistroLocalizacionUsu::class.java).apply {
                putExtra("nombres", nombres)
                putExtra("apellidos", apellidos)
                putExtra("edad", edad)
                putExtra("genero", genero)
                putExtra("tipoDocumento", tipoDoc)
                putExtra("documento", documento)
                putExtra("fechaNacimiento", fechaNac)
            }
            startActivity(intent)
        }
    }

    private fun showDatePickerDialog(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
            editText.setText(formattedDate)
            editText.error = null // Limpiar error si existía
        }, year, month, day)

        // Limitar para que no puedan elegir fechas futuras
        datePicker.datePicker.maxDate = System.currentTimeMillis()
        datePicker.show()
    }
}
