package com.example.projecteventclub.vista_usuario.main.usuarios

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.projecteventclub.R
import com.example.projecteventclub.SupaBaseClient
import com.example.projecteventclub.atributos.comida.Activity_comidas
import com.example.projecteventclub.atributos.eventos.consultar_evento.activity_ConEvUsuario
import com.example.projecteventclub.models.Evento
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class activity_usuarioPrincipal : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_main_user, container, false)

        // Botones de acción rápida
        setupActionButtons(view)

        // Cargar eventos dinámicos
        cargarEventosDestacados(view)

        return view
    }

    private fun setupActionButtons(view: View) {
        // Botón Localizar Evento - Actualizado para abrir el mapa normal en Bogotá
        view.findViewById<ImageButton>(R.id.btnLocEveMuser)?.setOnClickListener {
            // geo:lat,lng?z=zoom_level (z=13 es un buen nivel de ciudad)
            val gmmIntentUri = Uri.parse("geo:4.6097,-74.0817?z=13")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            
            if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(mapIntent)
            } else {
                // Alternativa en navegador si no hay app
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/@4.6097,-74.0817,13z"))
                startActivity(webIntent)
            }
        }

        // Botón Pedir Comida
        view.findViewById<ImageButton>(R.id.btnPedComMuser)?.setOnClickListener {
            val fragmentComidas = Activity_comidas()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentComidas)
                .addToBackStack(null)
                .commit()
        }

        // Botón Consultar Evento
        view.findViewById<ImageButton>(R.id.btnConEveMuser)?.setOnClickListener {
            val fragmentConsult = activity_ConEvUsuario()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentConsult)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun cargarEventosDestacados(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Obtenemos todos los eventos
                val listaEventos = SupaBaseClient.client.postgrest["eventos"]
                    .select()
                    .decodeList<Evento>()

                if (listaEventos.isNotEmpty()) {
                    // Mezclamos la lista para obtener aleatoriedad
                    val eventosAleatorios = listaEventos.shuffled().take(2)

                    // Mostrar primer evento
                    if (eventosAleatorios.size >= 1) {
                        actualizarPanelEvento(
                            view, 1,
                            eventosAleatorios[0]
                        )
                    }

                    // Mostrar segundo evento (si existe)
                    if (eventosAleatorios.size >= 2) {
                        actualizarPanelEvento(
                            view, 2,
                            eventosAleatorios[1]
                        )
                    } else {
                        // Si solo hay uno, ocultamos el segundo panel
                        view.findViewById<View>(R.id.cardEvent2)?.visibility = View.INVISIBLE
                    }
                }
            } catch (e: Exception) {
                // Error silencioso
            }
        }
    }

    private fun actualizarPanelEvento(view: View, index: Int, evento: Evento) {
        val suffix = if (index == 1) "1" else "2"
        
        val tvName = view.findViewById<TextView>(resources.getIdentifier("tvEventName$suffix", "id", context?.packageName))
        val tvDate = view.findViewById<TextView>(resources.getIdentifier("tvEventDate$suffix", "id", context?.packageName))
        val tvLocation = view.findViewById<TextView>(resources.getIdentifier("tvEventLocation$suffix", "id", context?.packageName))
        val tvTime = view.findViewById<TextView>(resources.getIdentifier("tvEventTime$suffix", "id", context?.packageName))

        tvName?.text = evento.nombre
        tvDate?.text = evento.fecha
        tvLocation?.text = evento.lugar
        tvTime?.text = evento.hora

        val card = view.findViewById<View>(resources.getIdentifier("cardEvent$suffix", "id", context?.packageName))
        card?.setOnClickListener {
            Toast.makeText(context, "Detalles de: ${evento.nombre}", Toast.LENGTH_SHORT).show()
        }
    }
}