package com.example.projecteventclub.atributos.comida

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.projecteventclub.R

class Activity_comidas : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.frag_comidas, container, false)

        val qr1 = view.findViewById<ImageView>(R.id.QR1)
        val qr2 = view.findViewById<ImageView>(R.id.QR2)
        val qr3 = view.findViewById<ImageView>(R.id.QR3)
        val qr4 = view.findViewById<ImageView>(R.id.QR4)

        // Datos simulados (pueden venir del usuario / evento)
        val localidad = "Tribuna Norte"
        val silla = "A-12"
        val usuarioId = "user_123"       // Luego usa auth.uid()
        val usuarioNombre = "Juan Pérez" // Nombre real

        qr1.setOnClickListener {
            abrirCarrito("McDonald's", localidad, silla, usuarioId, usuarioNombre)
        }

        qr2.setOnClickListener {
            abrirCarrito("KFC", localidad, silla, usuarioId, usuarioNombre)
        }

        qr3.setOnClickListener {
            abrirCarrito("El Corral", localidad, silla, usuarioId, usuarioNombre)
        }

        qr4.setOnClickListener {
            abrirCarrito("Pollo Frisby", localidad, silla, usuarioId, usuarioNombre)
        }

        return view
    }

    private fun abrirCarrito(
        proveedor: String,
        localidad: String,
        silla: String,
        usuarioId: String,
        usuarioNombre: String
    ) {
        val fragment = FragmentCarrito.newInstance(
            proveedor = proveedor,
            localidad = localidad,
            silla = silla,
            usuarioId = usuarioId,
            usuarioNombre = usuarioNombre
        )

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = Activity_comidas()
    }
}