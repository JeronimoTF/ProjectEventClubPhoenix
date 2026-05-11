package com.example.projecteventclub.atributos.anfitrion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projecteventclub.R
import com.example.projecteventclub.data.PedidoRepository
import kotlinx.coroutines.launch

class FragmentPedidosEntregados : Fragment() {

    private lateinit var adapter: PedidosAnfitrionAdapter
    private lateinit var tvSinEntregados: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pedidos_entregados, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rvPedidosEntregados)
        tvSinEntregados = view.findViewById(R.id.tvSinEntregados)

        view.findViewById<ImageView>(R.id.btnVolverEntregados).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        adapter = PedidosAnfitrionAdapter(
            pedidos = emptyList(),
            mostrarBotonEntregado = false
        )

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
        cargarPedidos()
        return view
    }

    private fun cargarPedidos() {
        lifecycleScope.launch {
            val result = PedidoRepository.obtenerPedidosEntregados()
            if (result.isSuccess) {
                val lista = result.getOrDefault(emptyList())
                adapter.actualizar(lista)
                tvSinEntregados.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cargarPedidos()
    }
}