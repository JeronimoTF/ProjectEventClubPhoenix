package com.example.projecteventclub.atributos.eventos.consultar_evento

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.projecteventclub.R
import com.example.projecteventclub.SupaBaseClient
import com.example.projecteventclub.models.Evento
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class activity_ConEvUsuario : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_consult_event_user1, container, false)

        val editNombre = view.findViewById<EditText>(R.id.EditTxtConEveUsu1)
        val btnBuscar = view.findViewById<ImageButton>(R.id.btnConEveUsu1)

        btnBuscar?.setOnClickListener {
            val nombreBuscar = editNombre?.text.toString()

            if (nombreBuscar.isNotEmpty()) {
                buscarEvento(nombreBuscar)
            } else {
                Toast.makeText(context, "Escribe el nombre del evento para buscar", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun buscarEvento(nombre: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Buscamos en Supabase
                val eventoEncontrado = SupaBaseClient.client.postgrest["eventos"]
                    .select {
                        filter {
                            eq("nombre", nombre)
                        }
                    }.decodeSingleOrNull<Evento>()

                if (eventoEncontrado != null) {
                    irADetalles(eventoEncontrado)
                } else {
                    Toast.makeText(context, "No se encontró el evento", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun irADetalles(evento: Evento) {
        val fragmentDetalles = activity_ConEvUsu2()
        
        // Pasamos todos los datos a la siguiente pantalla
        val bundle = Bundle()
        bundle.putString("nombre", evento.nombre)
        bundle.putString("descripcion", evento.descripcion)
        bundle.putString("fecha", evento.fecha)
        bundle.putString("hora", evento.hora)
        bundle.putString("lugar", evento.lugar)
        bundle.putString("localidad", evento.localidad)
        bundle.putString("pisoSilla", evento.pisoSilla)
        
        fragmentDetalles.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragmentDetalles)
            .addToBackStack(null)
            .commit()
    }
}
