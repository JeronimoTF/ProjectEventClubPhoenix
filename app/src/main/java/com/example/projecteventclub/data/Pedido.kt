package com.example.projecteventclub.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemPedido(
    val id: String? = null,
    @SerialName("pedido_id")
    val pedidoId: String? = null,
    val nombre: String = "",
    val cantidad: Int = 0,
    val precio: Double = 0.0
)

@Serializable
data class Pedido(
    val id: String? = null,
    @SerialName("usuario_id")
    val usuarioId: String = "",
    @SerialName("usuario_nombre")
    val usuarioNombre: String = "",
    val localidad: String = "",
    val silla: String = "",
    val proveedor: String = "",
    val total: Double = 0.0,
    val estado: String = "PENDIENTE",
    @SerialName("created_at")
    val createdAt: String? = null,
    val items: List<ItemPedido>? = null
)
