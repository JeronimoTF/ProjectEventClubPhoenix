package com.example.projecteventclub.atributos.eventos.crear_evento

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.projecteventclub.R
import com.example.projecteventclub.SupaBaseClient
import com.example.projecteventclub.models.Evento
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import java.util.Calendar

class activity_crearEvento : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_crear_event_adm, container, false)

        // 1. Identificamos todos los campos del formulario por su ID
        val editTxtNom = view.findViewById<EditText>(R.id.EditTxtNomEveAdm)
        val editTxtDes = view.findViewById<EditText>(R.id.EditTxtDesEveAdm)
        val editTxtFec = view.findViewById<EditText>(R.id.EditTxtFecEveAdn) // Nota: ID en XML es Adn
        val editTxtHor = view.findViewById<EditText>(R.id.EditTxtHorEveAdm)
        val editTxtLug = view.findViewById<EditText>(R.id.EditTxtLugEveAdm)
        val btnCrear = view.findViewById<ImageButton>(R.id.btnCreEveAdm)
        val btnBack = view.findViewById<ImageView>(R.id.btnBackCreEveAdm)

        // 2. Botón de volver atrás
        btnBack?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 3. Selector de Fecha (DatePickerDialog)
        editTxtFec?.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // Formateamos la fecha seleccionada
                val fecha = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editTxtFec.setText(fecha)
            }, year, month, day)
            dpd.show()
        }

        // 4. Selector de Hora (TimePickerDialog)
        editTxtHor?.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                // Formateamos la hora para que siempre tenga dos dígitos (ej: 08:05)
                val hora = String.format("%02d:%02d", selectedHour, selectedMinute)
                editTxtHor.setText(hora)
            }, hour, minute, true)
            tpd.show()
        }

        // 5. Acción para el campo de Lugar (Abrir Google Maps)
        editTxtLug?.setOnClickListener {
            if (editTxtLug.text.isNotEmpty()) {
                val gmmIntentUri = Uri.parse("geo:0,0?q=" + editTxtLug.text.toString())
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }

        // 6. Al hacer clic en el botón de "Check" (Crear Evento)
        btnCrear?.setOnClickListener {
            val nombre = editTxtNom?.text.toString().trim()
            val descripcion = editTxtDes?.text.toString().trim()
            val fecha = editTxtFec?.text.toString().trim()
            val hora = editTxtHor?.text.toString().trim()
            val lugar = editTxtLug?.text.toString().trim()

            // Validamos que los campos obligatorios no estén vacíos
            if (nombre.isNotEmpty() && fecha.isNotEmpty() && hora.isNotEmpty() && lugar.isNotEmpty()) {
                guardarEventoEnBaseDeDatos(nombre, descripcion, fecha, hora, lugar)
            } else {
                Toast.makeText(context, "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun guardarEventoEnBaseDeDatos(nom: String, des: String, fec: String, hor: String, lug: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val nuevoEvento = Evento(
                    nombre = nom,
                    descripcion = des,
                    fecha = fec,
                    hora = hor,
                    lugar = lug
                )

                SupaBaseClient.client.postgrest["eventos"].insert(nuevoEvento)
                irAPantallaExito()
                
            } catch (e: Exception) {
                Toast.makeText(context, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun irAPantallaExito() {
        val fragmentCreado = activity_EvenCreado()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragmentCreado)
            .addToBackStack(null)
            .commit()
    }
}
