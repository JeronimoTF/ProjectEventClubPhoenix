package com.example.projecteventclub.atributos.eventos.consultar_evento

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

class activity_consultarEvento : Fragment() {

    private var eventoActual: Evento? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_consult_event_adm, container, false)

        val editNombre = view.findViewById<EditText>(R.id.EditTxtNomEveAdm)
        val editDesc = view.findViewById<EditText>(R.id.EditTxDesEveAdm)
        val editFecHor = view.findViewById<EditText>(R.id.EditTxtIdEveAdm) 
        val editID = view.findViewById<EditText>(R.id.EditTxtFecHorEveAdm) 
        
        val btnLocalizar = view.findViewById<ImageButton>(R.id.btnEliEveAdm)
        val btnEditar = view.findViewById<ImageButton>(R.id.btnLocEveAdm)
        val btnEliminar = view.findViewById<ImageButton>(R.id.btnEdiEveAdm)
        val btnVolver = view.findViewById<ImageView>(R.id.btnBackConEveAdm)

        // Buscar al escribir el nombre y quitar el foco
        editNombre?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && editNombre.text.isNotEmpty()) {
                buscarEventoComoAdmin(editNombre.text.toString(), editDesc, editFecHor, editID)
            }
        }

        // BOTÓN EDITAR (ACTUALIZAR)
        btnEditar?.setOnClickListener {
            if (eventoActual != null) {
                val nuevoNombre = editNombre?.text.toString()
                val nuevaDesc = editDesc?.text.toString()
                // Aquí podrías separar fecha y hora si fuera necesario, 
                // por ahora lo manejamos simple para no complicarte.
                actualizarEvento(eventoActual!!.id!!, nuevoNombre, nuevaDesc)
            } else {
                Toast.makeText(context, "Busca un evento antes de editarlo", Toast.LENGTH_SHORT).show()
            }
        }

        btnEliminar?.setOnClickListener {
            eventoActual?.id?.let { eliminarEvento(it) }
        }

        btnVolver?.setOnClickListener { parentFragmentManager.popBackStack() }

        return view
    }

    private fun buscarEventoComoAdmin(nombre: String, editDesc: EditText?, editFecHor: EditText?, editID: EditText?) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val evento = SupaBaseClient.client.postgrest["eventos"]
                    .select { filter { eq("nombre", nombre) } }
                    .decodeSingleOrNull<Evento>()

                if (evento != null) {
                    eventoActual = evento
                    editDesc?.setText(evento.descripcion)
                    editFecHor?.setText("${evento.fecha} ${evento.hora}")
                    editID?.setText(evento.id.toString())
                } else {
                    Toast.makeText(context, "No encontrado", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actualizarEvento(id: Int, nuevoNom: String, nuevaDesc: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Le decimos a Supabase: "Busca este ID y pon estos datos nuevos"
                SupaBaseClient.client.postgrest["eventos"].update({
                    set("nombre", nuevoNom)
                    set("descripcion", nuevaDesc)
                }) {
                    filter { eq("id", id) }
                }
                Toast.makeText(context, "Evento actualizado con éxito", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error al actualizar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun eliminarEvento(id: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                SupaBaseClient.client.postgrest["eventos"].delete { filter { eq("id", id) } }
                Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
