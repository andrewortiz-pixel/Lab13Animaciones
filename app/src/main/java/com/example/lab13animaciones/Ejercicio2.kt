package com.example.lab13animaciones

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
fun Ejercicio2Screen(modifier: Modifier = Modifier) {
    var esRojo by remember { mutableStateOf(true) }
    var usarSpring by remember { mutableStateOf(false) }

    val colorAnimado by animateColorAsState(
        targetValue = if (esRojo) Color.Red else Color(0xFFC8A2C8),
        animationSpec = if (usarSpring) {
            spring(stiffness = Spring.StiffnessVeryLow)
        } else {
            tween(durationMillis = 1000)
        },
        label = "colorTriangulo"
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { esRojo = !esRojo }) {
            Text("Cambiar color")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { usarSpring = !usarSpring }) {
            Text(if (usarSpring) "Animación: spring" else "Animación: tween")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .size(150.dp)
                .clip(TriangleShape)
                .background(colorAnimado)
        ) {}
    }
}