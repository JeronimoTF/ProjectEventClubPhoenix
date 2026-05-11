package com.example.projecteventclub.atributos.anfitrion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projecteventclub.R
import com.example.projecteventclub.data.Pedido
import com.google.android.material.button.MaterialButton

class PedidosAnfitrionAdapter(
    private var pedidos: List<Pedido>,
    private val mostrarBotonEntregado: Boolean = true,
    private val onEntregado: ((Pedido) -> Unit)? = null
) : RecyclerView.Adapter<PedidosAnfitrionAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUsuario: TextView    = view.findViewById(R.id.tvUsuarioPedido)
        val tvProveedor: TextView  = view.findViewById(R.id.tvProveedorPedido)
        val tvUbicacion: TextView  = view.findViewById(R.id.tvUbicacionPedido)
        val tvItems: TextView      = view.findViewById(R.id.tvItemsPedido)
        val tvTotal: TextView      = view.findViewById(R.id.tvTotalPedido)
        val btnEntregado: MaterialButton = view.findViewById(R.id.btnEntregado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pedido_anfitrion, parent, false)
        )

    override fun getItemCount() = pedidos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.tvUsuario.text   = "👤 ${pedido.usuarioNombre}"
        holder.tvProveedor.text = "🍔 ${pedido.proveedor}"
        holder.tvUbicacion.text = "📍 ${pedido.localidad} — Silla: ${pedido.silla}"
        holder.tvItems.text = pedido.items?.joinToString("\n") {
            "• ${it.nombre} x${it.cantidad}  ($${String.format("%.0f", it.precio * it.cantidad)})"
        } ?: "Sin detalle"
        holder.tvTotal.text = "Total a cobrar: $${String.format("%.0f", pedido.total)}"

        if (mostrarBotonEntregado) {
            holder.btnEntregado.visibility = View.VISIBLE
            holder.btnEntregado.setOnClickListener { onEntregado?.invoke(pedido) }
        } else {
            holder.btnEntregado.visibility = View.GONE
        }
    }

    fun actualizar(nuevos: List<Pedido>) {
        pedidos = nuevos
        notifyDataSetChanged()
    }
}