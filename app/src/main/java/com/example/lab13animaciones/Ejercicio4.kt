package com.example.lab13animaciones

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class EstadoPantalla {
    Cargando,
    Contenido,
    Error
}

@Composable
fun Ejercicio4Screen(modifier: Modifier = Modifier) {
    var estado by remember { mutableStateOf(EstadoPantalla.Cargando) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            estado = when (estado) {
                EstadoPantalla.Cargando -> EstadoPantalla.Contenido
                EstadoPantalla.Contenido -> EstadoPantalla.Error
                EstadoPantalla.Error -> EstadoPantalla.Cargando
            }
        }) {
            Text("Cambiar estado")
        }

        Spacer(modifier = Modifier.height(48.dp))

        AnimatedContent(
            targetState = estado,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 600, delayMillis = 300)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = 300))
            },
            label = "animacionEstado"
        ) { estadoActual ->
            when (estadoActual) {
                EstadoPantalla.Cargando -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Cargando...", fontSize = 20.sp)
                    }
                }

                EstadoPantalla.Contenido -> {
                    Text(
                        text = "Contenido cargado correctamente",
                        fontSize = 20.sp,
                        color = Color(0xFF2E7D32)
                    )
                }

                EstadoPantalla.Error -> {
                    Text(
                        text = "Ocurrió un error al cargar",
                        fontSize = 20.sp,
                        color = Color.Red
                    )
                }
            }
        }
    }
}