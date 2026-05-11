package com.example.projecteventclub.atributos.anfitrion

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
import com.example.projecteventclub.data.PedidoRepository
import kotlinx.coroutines.launch

class FragmentPedidosPendientes : Fragment() {

    private lateinit var adapter: PedidosAnfitrionAdapter
    private lateinit var tvSinPedidos: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pedidos_pendientes, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rvPedidosPendientes)
        tvSinPedidos = view.findViewById(R.id.tvSinPedidos)

        // Botón volver
        view.findViewById<ImageView>(R.id.btnVolverPendientes).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        adapter = PedidosAnfitrionAdapter(
            pedidos = emptyList(),
            mostrarBotonEntregado = true,
            onEntregado = { pedido ->
                lifecycleScope.launch {
                    val result = PedidoRepository.marcarEntregado(pedido.id!!)
                    if (result.isSuccess) {
                        Toast.makeText(requireContext(),
                            "Pedido entregado ✓", Toast.LENGTH_SHORT).show()
                        cargarPedidos()
                    } else {
                        Toast.makeText(requireContext(),
                            "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
        cargarPedidos()
        return view
    }

    private fun cargarPedidos() {
        lifecycleScope.launch {
            val result = PedidoRepository.obtenerPedidosPendientes()
            if (result.isSuccess) {
                val lista = result.getOrDefault(emptyList())
                adapter.actualizar(lista)
                // Mostrar mensaje si no hay pedidos
                tvSinPedidos.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cargarPedidos()
    }
}