package com.example.projecteventclub.atributos.comida

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projecteventclub.R
import com.example.projecteventclub.data.Pedido
import com.example.projecteventclub.data.PedidoRepository
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import com.example.projecteventclub.atributos.experiencia.inicio.homeEventFragment

class FragmentCarrito : Fragment() {

    companion object {
        val MENUS = mapOf(
            "McDonald's" to listOf(
                ItemMenu("Big Mac", 25000.0),
                ItemMenu("McPollo", 22000.0),
                ItemMenu("Papas medianas", 12000.0),
                ItemMenu("Gaseosa", 8000.0)
            ),
            "KFC" to listOf(
                ItemMenu("Combo 2 piezas", 28000.0),
                ItemMenu("Combo Familiar", 65000.0),
                ItemMenu("Alitas x6", 30000.0),
                ItemMenu("Gaseosa", 8000.0)
            ),
            "El Corral" to listOf(
                ItemMenu("Corral Clásico", 32000.0),
                ItemMenu("Perro Corral", 20000.0),
                ItemMenu("Papas", 10000.0),
                ItemMenu("Gaseosa", 8000.0)
            ),
            "Pollo Frisby" to listOf(
                ItemMenu("Pechuga", 24000.0),
                ItemMenu("Combo Familiar", 58000.0),
                ItemMenu("Arroz", 8000.0),
                ItemMenu("Gaseosa", 8000.0)
            )
        )

        fun newInstance(
            proveedor: String,
            localidad: String,
            silla: String,
            usuarioId: String,
            usuarioNombre: String
        ) = FragmentCarrito().apply {
            arguments = Bundle().apply {
                putString("proveedor", proveedor)
                putString("localidad", localidad)
                putString("silla", silla)
                putString("usuarioId", usuarioId)
                putString("usuarioNombre", usuarioNombre)
            }
        }
    }

    private lateinit var adapter: MenuCarritoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_carrito, container, false)

        val proveedor     = arguments?.getString("proveedor") ?: ""
        val localidad     = arguments?.getString("localidad") ?: ""
        val silla         = arguments?.getString("silla") ?: ""
        val usuarioId     = arguments?.getString("usuarioId") ?: ""
        val usuarioNombre = arguments?.getString("usuarioNombre") ?: ""

        val tvProveedor = view.findViewById<TextView>(R.id.tvProveedorCarrito)
        val tvTotal     = view.findViewById<TextView>(R.id.tvTotal)
        val rv          = view.findViewById<RecyclerView>(R.id.rvItemsMenu)
        val btnCrear    = view.findViewById<MaterialButton>(R.id.btnCrearPedido)
        val btnVolver   = view.findViewById<ImageView>(R.id.btnVolverhome)

        // ✅ BOTÓN VOLVER FUNCIONAL (al Home real)
        btnVolver.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeEventFragment())
                .commit()
        }

        tvProveedor.text = proveedor

        val items = MENUS[proveedor]?.map { it.copy() } ?: emptyList()

        adapter = MenuCarritoAdapter(items) {
            tvTotal.text = "$${String.format("%.0f", adapter.calcularTotal())}"
        }

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        btnCrear.setOnClickListener {
            val seleccionados = adapter.obtenerItemsSeleccionados()
            if (seleccionados.isEmpty()) {
                Toast.makeText(requireContext(),
                    "Agrega al menos un ítem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val pedido = Pedido(
                usuarioId     = usuarioId,
                usuarioNombre = usuarioNombre,
                localidad     = localidad,
                silla         = silla,
                proveedor     = proveedor,
                total         = adapter.calcularTotal()
            )

            btnCrear.isEnabled = false

            lifecycleScope.launch {
                val resultado = PedidoRepository.crearPedido(pedido, seleccionados)
                if (resultado.isSuccess) {
                    Toast.makeText(requireContext(),
                        "¡Pedido creado!", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, homeEventFragment())
                        .commit()
                } else {
                    Toast.makeText(requireContext(),
                        "Error al crear pedido", Toast.LENGTH_SHORT).show()
                    btnCrear.isEnabled = true
                }
            }
        }

        return view
    }
}