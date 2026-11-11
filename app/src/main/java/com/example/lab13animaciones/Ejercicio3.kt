package com.example.lab13animaciones

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Ejercicio3Screen(modifier: Modifier = Modifier) {
    var activo by remember { mutableStateOf(false) }
    var ordenB by remember { mutableStateOf(false) }

    val tamano by animateDpAsState(
        targetValue = if (activo) 160.dp else 80.dp,
        animationSpec = tween(durationMillis = 800),
        label = "tamano"
    )

    val posX by animateDpAsState(
        targetValue = if (activo) 120.dp else 0.dp,
        animationSpec = tween(durationMillis = 800),
        label = "posX"
    )
    val posY by animateDpAsState(
        targetValue = if (activo) 60.dp else 0.dp,
        animationSpec = tween(durationMillis = 800),
        label = "posY"
    )

    val modificadorA = Modifier
        .offset(x = posX, y = posY)
        .size(tamano)
        .clip(TriangleShape)
        .background(Color.Red)

    val modificadorB = Modifier
        .clip(TriangleShape)
        .background(Color.Red)
        .offset(x = posX, y = posY)
        .size(tamano)

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { activo = !activo }) {
            Text("Mover y cambiar tamaño")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { ordenB = !ordenB }) {
            Text(if (ordenB) "Orden B: fondo y luego offset" else "Orden A: offset y luego fondo")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .padding(start = 24.dp)
        ) {
            Box(
                modifier = if (ordenB) modificadorB else modificadorA,
                contentAlignment = Alignment.BottomCenter
            ) {
                Text("Hola", color = Color.White)
            }
        }
    }
}