package com.example.projecteventclub.atributos.eventos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projecteventclub.R
import com.example.projecteventclub.SupaBaseClient
import com.example.projecteventclub.models.Asistencia
import com.example.projecteventclub.models.Evento
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class MisEventosFragment : Fragment() {

    private lateinit var rvMisEventos: RecyclerView
    private lateinit var adapter: MisEventosAdapter
    private val listaEventos = mutableListOf<Evento>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mis_eventos, container, false)

        rvMisEventos = view.findViewById(R.id.rvMisEventos)
        val btnVolver = view.findViewById<ImageView>(R.id.btnBackMisEventos)

        btnVolver?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupRecyclerView()
        cargarMisEventos()

        return view
    }

    private fun setupRecyclerView() {
        rvMisEventos.layoutManager = LinearLayoutManager(context)
        adapter = MisEventosAdapter(listaEventos) { evento ->
            Toast.makeText(requireContext(), "Mostrando QR para: ${evento.nombre}", Toast.LENGTH_SHORT).show()
        }
        rvMisEventos.adapter = adapter
    }

    private fun cargarMisEventos() {
        val userId = SupaBaseClient.client.auth.currentUserOrNull()?.id ?: return

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // 1. Obtener las asistencias del usuario
                val asistencias = withContext(Dispatchers.IO) {
                    SupaBaseClient.client.postgrest["asistencias"]
                        .select {
                            filter { eq("usuario_id", userId) }
                        }.decodeList<Asistencia>()
                }

                if (asistencias.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Aún no tienes eventos marcados", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                // 2. Extraer los IDs de los eventos (asegurando que no sean nulos)
                val idsEventos = asistencias.mapNotNull { it.eventoId }.distinct()
                
                if (idsEventos.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "No se encontraron IDs de eventos válidos", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                // 3. Consultar los detalles de esos eventos
                val eventos = withContext(Dispatchers.IO) {
                    SupaBaseClient.client.postgrest["eventos"]
                        .select {
                            filter { 
                                isIn("id", idsEventos) 
                            }
                        }.decodeList<Evento>()
                }

                withContext(Dispatchers.Main) {
                    listaEventos.clear()
                    listaEventos.addAll(eventos)
                    adapter.notifyDataSetChanged()
                    
                    if (eventos.isEmpty()) {
                        Toast.makeText(context, "No se encontraron detalles para tus eventos", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                Log.e("MisEventos", "Error cargando eventos", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
