package com.example.projecteventclub.atributos.eventos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projecteventclub.R
import com.example.projecteventclub.models.Evento

class MisEventosAdapter(
    private val eventos: List<Evento>,
    private val onVerQRClick: (Evento) -> Unit
) : RecyclerView.Adapter<MisEventosAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreEvento)
        val tvFecha: TextView = view.findViewById(R.id.tvFechaHora)
        val tvLugar: TextView = view.findViewById(R.id.tvLugar)
        val tvDesc: TextView = view.findViewById(R.id.tvDescripcion)
        val btnQR: Button = view.findViewById(R.id.btnVerQR)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mis_eventos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val evento = eventos[position]
        holder.tvNombre.text = evento.nombre
        holder.tvFecha.text = "${evento.fecha} - ${evento.hora}"
        holder.tvLugar.text = evento.lugar
        holder.tvDesc.text = evento.descripcion
        
        holder.btnQR.setOnClickListener { onVerQRClick(evento) }
    }

    override fun getItemCount() = eventos.size
}
