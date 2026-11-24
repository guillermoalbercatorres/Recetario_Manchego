package com.example.recetariomanchego.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.recetariomanchego.data.Receta
import com.example.recetariomanchego.data.recetasIniciales
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecetasViewModel : ViewModel() {
    // Estado privado mutable / estado público inmutable (patrón visto en P5)
    private val _recetas = MutableStateFlow(recetasIniciales())
    val recetas: StateFlow<List<Receta>> = _recetas.asStateFlow()
    // Id de la tarjeta expandida. null = ninguna.
    private val _expandidaId = MutableStateFlow<Int?>(null)
    val expandidaId: StateFlow<Int?> = _expandidaId.asStateFlow()
    // Pulsar la tarjeta expandida la contrae; pulsar otra, cambia la expandida.
    fun alternarExpandida(id: Int) {
        _expandidaId.value = if (_expandidaId.value == id) null else id
    }
    fun recetaConId(id: Int): Receta? = _recetas.value.firstOrNull { it.id == id }

    fun alternarFavorita(id: Int){
        _recetas.update { lista ->
            lista.map { if (it.id == id) it.copy(esFavorita = !it.esFavorita) else it }
        }
    }

}