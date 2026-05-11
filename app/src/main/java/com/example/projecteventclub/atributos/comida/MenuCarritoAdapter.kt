package com.example.projecteventclub.atributos.comida

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projecteventclub.R
import com.example.projecteventclub.data.ItemPedido

data class ItemMenu(
    val nombre: String,
    val precio: Double,
    var cantidad: Int = 0
)

class MenuCarritoAdapter(
    private val items: List<ItemMenu>,
    private val onCantidadCambia: () -> Unit
) : RecyclerView.Adapter<MenuCarritoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreItem)
        val tvPrecio: TextView = view.findViewById(R.id.tvPrecioItem)
        val tvCantidad: TextView = view.findViewById(R.id.tvCantidad)
        val btnMas: ImageButton = view.findViewById(R.id.btnMas)
        val btnMenos: ImageButton = view.findViewById(R.id.btnMenos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_menu_carrito, parent, false)
        )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvNombre.text = item.nombre
        holder.tvPrecio.text = "$${String.format("%.0f", item.precio)}"
        holder.tvCantidad.text = item.cantidad.toString()

        holder.btnMas.setOnClickListener {
            item.cantidad++
            holder.tvCantidad.text = item.cantidad.toString()
            onCantidadCambia()
        }

        holder.btnMenos.setOnClickListener {
            if (item.cantidad > 0) {
                item.cantidad--
                holder.tvCantidad.text = item.cantidad.toString()
                onCantidadCambia()
            }
        }
    }

    fun obtenerItemsSeleccionados(): List<ItemPedido> =
        items.filter { it.cantidad > 0 }
            .map { ItemPedido(nombre = it.nombre, cantidad = it.cantidad, precio = it.precio) }

    fun calcularTotal(): Double = items.sumOf { it.precio * it.cantidad }
}