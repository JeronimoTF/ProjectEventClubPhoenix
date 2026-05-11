package com.example.projecteventclub.data

import com.example.projecteventclub.SupaBaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

object PedidoRepository {

    private val client = SupaBaseClient.client

    suspend fun crearPedido(pedido: Pedido, items: List<ItemPedido>): Result<String> {
        return try {
            val insertado = client.from("pedidos")
                .insert(pedido) { select() }
                .decodeSingle<Pedido>()

            val pedidoId = insertado.id!!
            val itemsConId = items.map { it.copy(pedidoId = pedidoId) }
            client.from("items_pedido").insert(itemsConId)

            Result.success(pedidoId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerPedidosPendientes(): Result<List<Pedido>> {
        return try {
            val lista = client.from("pedidos")
                .select {
                    filter { eq("estado", "PENDIENTE") }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<Pedido>()
            Result.success(lista)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerPedidosEntregados(): Result<List<Pedido>> {
        return try {
            val lista = client.from("pedidos")
                .select {
                    filter { eq("estado", "ENTREGADO") }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<Pedido>()
            Result.success(lista)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerItemsDePedido(pedidoId: String): Result<List<ItemPedido>> {
        return try {
            val lista = client.from("items_pedido")
                .select { filter { eq("pedido_id", pedidoId) } }
                .decodeList<ItemPedido>()
            Result.success(lista)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun marcarEntregado(pedidoId: String): Result<Unit> {
        return try {
            client.from("pedidos")
                .update({ set("estado", "ENTREGADO") }) {
                    filter { eq("id", pedidoId) }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}