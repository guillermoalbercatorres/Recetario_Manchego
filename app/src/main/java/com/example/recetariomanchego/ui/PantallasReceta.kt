package com.example.recetariomanchego.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.recetariomanchego.R
import com.example.recetariomanchego.data.Receta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRecetasScreen(
    viewModel: RecetasViewModel,
    onVerFicha: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val recetas by viewModel.recetas.collectAsStateWithLifecycle()
    val expandidaId by viewModel.expandidaId.collectAsStateWithLifecycle()
    var textoBusqueda by rememberSaveable { mutableStateOf("") }

    val recetasFiltradas = recetas.filter {
        it.nombre.contains(textoBusqueda, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.titulo_lista)) })
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                label = { Text(stringResource(R.string.buscar_receta)) },
                singleLine = true,
                trailingIcon = {
                    if (textoBusqueda.isNotEmpty()) {
                        IconButton(onClick = { textoBusqueda = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.borrar_busqueda)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            if (recetasFiltradas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.sin_resultados_busqueda),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = recetasFiltradas, key = { it.id }) { receta ->
                        TarjetaReceta(
                            receta = receta,
                            expandida = expandidaId == receta.id,
                            onAlternar = { viewModel.alternarExpandida(receta.id) },
                            onVerFicha = { onVerFicha(receta.id) },
                            onAlternarFavorita = { viewModel.alternarFavorita(receta.id) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FichaRecetaScreen(
    receta: Receta,
    onVolver: () -> Unit,
    onAlternarFavorita: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Punto crítico de adaptabilidad: se decide la disposición según la orientación.
    val configuracion = LocalConfiguration.current
    val esHorizontal = configuracion.orientation == Configuration.ORIENTATION_LANDSCAPE

    val colorCorazon by animateColorAsState(
        targetValue = if (receta.esFavorita) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "colorFavoritaFicha"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(receta.nombre) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.volver)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onAlternarFavorita) {
                        Icon(
                            imageVector = if (receta.esFavorita) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = stringResource(R.string.favorita),
                            tint = colorCorazon
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (esHorizontal) {
            DisposicionHorizontal(
                receta = receta,
                modifier = modifier.padding(padding)
            )
        } else {
            DisposicionVertical(
                receta = receta,
                modifier = modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun DisposicionVertical(
    receta: Receta,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        BloqueFoto(
            receta = receta,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        ContenidoReceta(receta = receta)
    }
}

@Composable
private fun DisposicionHorizontal(
    receta: Receta,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BloqueFoto(
            receta = receta,
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight()
        )
        Column(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            ContenidoReceta(receta = receta)
        }
    }
}

// Contenido compartido por ambas disposiciones: se escribe una sola vez.
// Se invoca siempre dentro de un Column, que es quien lo apila.
@Composable
private fun ContenidoReceta(receta: Receta) {
    BloqueResumen(receta = receta)
    Spacer(modifier = Modifier.height(16.dp))
    BloqueIngredientes(ingredientes = receta.ingredientes)
    Spacer(modifier = Modifier.height(16.dp))
    BloquePasos(pasos = receta.pasos)
}