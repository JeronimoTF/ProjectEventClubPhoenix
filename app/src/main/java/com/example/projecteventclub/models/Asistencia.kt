package com.example.projecteventclub.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Asistencia(
    val id: Long? = null,
    @SerialName("usuario_id")
    val usuarioId: String? = null,
    @SerialName("evento_id")
    val eventoId: Long? = null,
    @SerialName("fecha_registro")
    val fechaRegistro: String? = null,
    val eventos: Evento? = null // Relación con la tabla eventos
)
