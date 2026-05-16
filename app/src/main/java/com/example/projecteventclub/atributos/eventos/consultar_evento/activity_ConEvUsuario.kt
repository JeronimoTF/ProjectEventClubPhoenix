package com.example.projecteventclub.atributos.eventos.consultar_evento

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.projecteventclub.R
import com.example.projecteventclub.SupaBaseClient
import com.example.projecteventclub.models.Evento
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class activity_ConEvUsuario : Fragment() {

    private var eventoActual: Evento? = null

    // Referencias a la UI
    private lateinit var etBuscarNombre: EditText
    private lateinit var btnBuscar: ImageButton
    private lateinit var cardGestion: CardView
    private lateinit var tvNombreSeleccionado: TextView
    private lateinit var llConsultar: LinearLayout
    
    private lateinit var llDetalles: LinearLayout
    private lateinit var etDesc: EditText
    private lateinit var etFecha: EditText
    private lateinit var etLugar: EditText
    private lateinit var btnVerEnMapa: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_consult_event_user1, container, false)

        // Inicializar vistas
        etBuscarNombre = view.findViewById(R.id.EditTxtConEveUsu1)
        btnBuscar = view.findViewById(R.id.btnConEveUsu1)
        cardGestion = view.findViewById(R.id.cardGestionEventoUsu)
        tvNombreSeleccionado = view.findViewById(R.id.tvEventoSeleccionadoUsu)
        llConsultar = view.findViewById(R.id.llBtnConsultarUsu)
        
        llDetalles = view.findViewById(R.id.llDetallesEventoUsu)
        etDesc = view.findViewById(R.id.etDetalleDescUsu)
        etFecha = view.findViewById(R.id.etDetalleFechaUsu)
        etLugar = view.findViewById(R.id.etDetalleLugarUsu)
        btnVerEnMapa = view.findViewById(R.id.btnVerEnMapaUsu)
        
        val btnVolver = view.findViewById<ImageView>(R.id.btnBackConEveUsu1)

        // Acción de búsqueda
        btnBuscar.setOnClickListener {
            val nombre = etBuscarNombre.text.toString().trim()
            if (nombre.isNotEmpty()) {
                buscarEvento(nombre)
            } else {
                Toast.makeText(context, "Ingresa un nombre para buscar", Toast.LENGTH_SHORT).show()
            }
        }

        // ACCIÓN: VER DETALLES (Muestra la sección de detalles)
        llConsultar.setOnClickListener {
            llDetalles.visibility = View.VISIBLE
            Toast.makeText(context, "Mostrando detalles", Toast.LENGTH_SHORT).show()
        }

        // BOTÓN: VER EN MAPA
        btnVerEnMapa.setOnClickListener {
            val lugar = etLugar.text.toString().trim()
            if (lugar.isNotEmpty()) {
                abrirGoogleMaps(lugar)
            } else {
                Toast.makeText(context, "No hay una ubicación definida para este evento", Toast.LENGTH_SHORT).show()
            }
        }

        btnVolver?.setOnClickListener { parentFragmentManager.popBackStack() }

        return view
    }

    private fun abrirGoogleMaps(ubicacion: String) {
        // Se centra en Bogotá (4.6097, -74.0817) y busca el lugar sin iniciar navegación
        val gmmIntentUri = Uri.parse("geo:4.6097,-74.0817?q=$ubicacion")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        
        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        } else {
            // Alternativa en web centrada en Bogotá
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=$ubicacion&center=4.6097,-74.0817"))
            startActivity(webIntent)
        }
    }

    private fun buscarEvento(nombre: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val evento = SupaBaseClient.client.postgrest["eventos"]
                    .select { filter { eq("nombre", nombre) } }
                    .decodeSingleOrNull<Evento>()

                if (evento != null) {
                    eventoActual = evento
                    tvNombreSeleccionado.text = evento.nombre
                    etDesc.setText(evento.descripcion)
                    etFecha.setText("${evento.fecha} ${evento.hora}")
                    etLugar.setText(evento.lugar)
                    
                    // Mostramos la barra de resultado y ocultamos detalles por ahora
                    cardGestion.visibility = View.VISIBLE
                    llDetalles.visibility = View.GONE
                } else {
                    cardGestion.visibility = View.GONE
                    llDetalles.visibility = View.GONE
                    Toast.makeText(context, "Evento no encontrado", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}