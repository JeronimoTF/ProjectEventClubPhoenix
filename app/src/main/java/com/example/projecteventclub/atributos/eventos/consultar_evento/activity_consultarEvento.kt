package com.example.projecteventclub.atributos.eventos.consultar_evento

import android.app.AlertDialog
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

class activity_consultarEvento : Fragment() {

    private var eventoActual: Evento? = null

    // Referencias a la UI
    private lateinit var etBuscarNombre: EditText
    private lateinit var btnBuscar: ImageButton
    private lateinit var cardGestion: CardView
    private lateinit var tvNombreSeleccionado: TextView
    
    private lateinit var llConsultar: LinearLayout
    private lateinit var llEditar: LinearLayout
    private lateinit var llEliminar: LinearLayout
    
    private lateinit var llDetalles: LinearLayout
    private lateinit var etDesc: EditText
    private lateinit var etFecha: EditText
    private lateinit var etLugar: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnVerEnMapa: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_consult_event_adm, container, false)

        // Inicializar vistas
        etBuscarNombre = view.findViewById(R.id.etBuscarNombre)
        btnBuscar = view.findViewById(R.id.btnBuscarEvento)
        cardGestion = view.findViewById(R.id.cardGestionEvento)
        tvNombreSeleccionado = view.findViewById(R.id.tvEventoSeleccionado)
        
        llConsultar = view.findViewById(R.id.llBtnConsultar)
        llEditar = view.findViewById(R.id.llBtnEditar)
        llEliminar = view.findViewById(R.id.llBtnEliminar)
        
        llDetalles = view.findViewById(R.id.llDetallesEvento)
        etDesc = view.findViewById(R.id.etDetalleDesc)
        etFecha = view.findViewById(R.id.etDetalleFecha)
        etLugar = view.findViewById(R.id.etDetalleLugar)
        btnGuardar = view.findViewById(R.id.btnGuardarCambios)
        btnVerEnMapa = view.findViewById(R.id.btnVerEnMapa)
        
        val btnVolver = view.findViewById<ImageView>(R.id.btnBackConEveAdm)

        // Acción de búsqueda
        btnBuscar.setOnClickListener {
            val nombre = etBuscarNombre.text.toString().trim()
            if (nombre.isNotEmpty()) {
                buscarEvento(nombre)
            } else {
                Toast.makeText(context, "Ingresa un nombre para buscar", Toast.LENGTH_SHORT).show()
            }
        }

        // ACCIÓN: CONSULTAR (Modo Lectura)
        llConsultar.setOnClickListener {
            configurarModo(editable = false)
        }

        // ACCIÓN: EDITAR (Modo Escritura)
        llEditar.setOnClickListener {
            configurarModo(editable = true)
        }

        // ACCIÓN: ELIMINAR (Con Pop-up de confirmación)
        llEliminar.setOnClickListener {
            mostrarConfirmacionEliminar()
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

        // BOTÓN: GUARDAR CAMBIOS
        btnGuardar.setOnClickListener {
            val id = eventoActual?.id
            if (id != null) {
                actualizarEvento(
                    id,
                    etDesc.text.toString(),
                    etFecha.text.toString(),
                    etLugar.text.toString()
                )
            }
        }

        btnVolver?.setOnClickListener { parentFragmentManager.popBackStack() }

        return view
    }

    private fun abrirGoogleMaps(ubicacion: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=$ubicacion")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        
        // Verificamos si hay alguna app que pueda manejar el intent
        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        } else {
            // Si no tiene Google Maps, intentamos abrir el navegador
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=$ubicacion"))
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
                    
                    // Mostramos la barra de gestión y ocultamos detalles por ahora
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

    private fun configurarModo(editable: Boolean) {
        llDetalles.visibility = View.VISIBLE
        
        etDesc.isEnabled = editable
        etFecha.isEnabled = editable
        etLugar.isEnabled = editable
        
        // El botón guardar solo aparece en modo edición
        btnGuardar.visibility = if (editable) View.VISIBLE else View.GONE
        
        val modoStr = if (editable) "Modo Edición" else "Modo Consulta"
        Toast.makeText(context, modoStr, Toast.LENGTH_SHORT).show()
    }

    private fun mostrarConfirmacionEliminar() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Eliminar Evento")
        builder.setMessage("¿Estás seguro de que deseas eliminar este evento? Esta acción no se puede deshacer.")
        
        builder.setPositiveButton("ACEPTAR") { _, _ ->
            eventoActual?.id?.let { eliminarEvento(it) }
        }
        
        builder.setNegativeButton("RECHAZAR") { dialog, _ ->
            dialog.dismiss()
        }
        
        val dialog = builder.create()
        dialog.show()
    }

    private fun actualizarEvento(id: Int, nuevaDesc: String, nuevaFec: String, nuevoLug: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                SupaBaseClient.client.postgrest["eventos"].update({
                    set("descripcion", nuevaDesc)
                    set("fecha", nuevaFec)
                    set("lugar", nuevoLug)
                }) {
                    filter { eq("id", id) }
                }
                Toast.makeText(context, "Evento actualizado con éxito", Toast.LENGTH_SHORT).show()
                configurarModo(editable = false) // Volvemos a modo lectura
            } catch (e: Exception) {
                Toast.makeText(context, "Error al actualizar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun eliminarEvento(id: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                SupaBaseClient.client.postgrest["eventos"].delete { filter { eq("id", id) } }
                Toast.makeText(context, "Evento eliminado correctamente", Toast.LENGTH_SHORT).show()
                
                // Limpiamos la UI
                cardGestion.visibility = View.GONE
                llDetalles.visibility = View.GONE
                etBuscarNombre.setText("")
            } catch (e: Exception) {
                Toast.makeText(context, "Error al eliminar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
