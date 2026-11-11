package com.example.lab13animaciones

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// Estados del videojuego
enum class EstadoJuego {
    Menu,
    Jugando,
    Victoria,
    GameOver
}

@Composable
fun EjercicioFinalScreen(modifier: Modifier = Modifier) {
    var estado by remember { mutableStateOf(EstadoJuego.Menu) }
    var vidas by remember { mutableStateOf(3) }
    var puntos by remember { mutableStateOf(0) }
    var carrilJugador by remember { mutableStateOf(1) }
    var carrilEnemigo by remember { mutableStateOf(1) }
    var enemigoVisible by remember { mutableStateOf(false) }
    var enemigoCayendo by remember { mutableStateOf(false) }
    var golpeado by remember { mutableStateOf(false) }

    val puntosParaGanar = 5
    val anchoCarril = 100.dp
    val altoArena = 380.dp

    fun reiniciar() {
        vidas = 3
        puntos = 0
        carrilJugador = 1
        golpeado = false
        enemigoCayendo = false
        enemigoVisible = false
        estado = EstadoJuego.Jugando
    }

    // Bucle del juego: cada ronda cae un meteorito en un carril al azar
    LaunchedEffect(estado) {
        if (estado == EstadoJuego.Jugando) {
            while (true) {
                carrilEnemigo = (0..2).random()
                enemigoVisible = true
                enemigoCayendo = true
                delay(1500)

                if (carrilEnemigo == carrilJugador) {
                    vidas -= 1
                    golpeado = true
                } else {
                    puntos += 1
                }

                enemigoCayendo = false
                enemigoVisible = false
                delay(400)
                golpeado = false

                if (vidas <= 0) {
                    estado = EstadoJuego.GameOver
                    break
                }
                if (puntos >= puntosParaGanar) {
                    estado = EstadoJuego.Victoria
                    break
                }
            }
        }
    }

    // animateDpAsState: el triángulo crece con cada punto
    val tamanoJugador by animateDpAsState(
        targetValue = 50.dp + (puntos * 8).dp,
        animationSpec = tween(durationMillis = 400),
        label = "tamanoJugador"
    )

    // animateDpAsState con spring: movimiento entre carriles con efecto rebote
    val xCarril by animateDpAsState(
        targetValue = anchoCarril * carrilJugador,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "xCarril"
    )

    // animateColorAsState: el triángulo se pone lila cuando recibe daño
    val colorJugador by animateColorAsState(
        targetValue = if (golpeado) Color(0xFFC8A2C8) else Color.Red,
        animationSpec = tween(durationMillis = 200),
        label = "colorJugador"
    )

    // animateDpAsState: caída del meteorito, y vuelve arriba al instante con snap
    val yEnemigo by animateDpAsState(
        targetValue = if (enemigoCayendo) altoArena - 70.dp else 0.dp,
        animationSpec = if (enemigoCayendo) {
            tween(durationMillis = 1500, easing = LinearEasing)
        } else {
            snap()
        },
        label = "yEnemigo"
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "TRIÁNGULO ESPACIAL",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Marcador: puntos y vidas. Cada corazón usa AnimatedVisibility
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Puntos: $puntos / $puntosParaGanar", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(24.dp))
            repeat(3) { i ->
                AnimatedVisibility(
                    visible = vidas > i,
                    enter = fadeIn(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(400))
                ) {
                    Text(text = "❤️", fontSize = 22.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // AnimatedContent: transición entre las pantallas del juego
        AnimatedContent(
            targetState = estado,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 500, delayMillis = 200)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = 200))
            },
            label = "pantallaJuego"
        ) { estadoActual ->
            when (estadoActual) {
                EstadoJuego.Menu -> {
                    Column(
                        modifier = Modifier.size(width = anchoCarril * 3, height = altoArena),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Esquiva los meteoritos", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Consigue $puntosParaGanar puntos sin perder tus 3 vidas")
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = { reiniciar() }) {
                            Text("Empezar")
                        }
                    }
                }

                EstadoJuego.Jugando -> {
                    Box(
                        modifier = Modifier
                            .size(width = anchoCarril * 3, height = altoArena)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        // Fondo con tres carriles
                        Row {
                            repeat(3) { i ->
                                Box(
                                    modifier = Modifier
                                        .size(width = anchoCarril, height = altoArena)
                                        .background(
                                            if (i % 2 == 0) Color(0xFF1B1B2F) else Color(0xFF252545)
                                        )
                                )
                            }
                        }

                        // Meteorito: aparece y desaparece con AnimatedVisibility
                        androidx.compose.animation.AnimatedVisibility(
                            visible = enemigoVisible,
                            enter = androidx.compose.animation.fadeIn(
                                animationSpec = androidx.compose.animation.core.tween(durationMillis = 300)
                            ),
                            exit = androidx.compose.animation.fadeOut(
                                animationSpec = androidx.compose.animation.core.tween(durationMillis = 200)
                            ),
                            modifier = Modifier.offset(
                                x = anchoCarril * carrilEnemigo + 30.dp,
                                y = yEnemigo
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFC107))
                            )
                        }

                        // Jugador: triángulo que se mueve, crece y cambia de color
                        Box(
                            modifier = Modifier
                                .offset(
                                    x = xCarril + (anchoCarril - tamanoJugador) / 2,
                                    y = altoArena - 10.dp - tamanoJugador
                                )
                                .size(tamanoJugador)
                                .clip(TriangleShape)
                                .background(colorJugador)
                        )
                    }
                }

                EstadoJuego.Victoria -> {
                    Column(
                        modifier = Modifier.size(width = anchoCarril * 3, height = altoArena),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "¡Ganaste!",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = { reiniciar() }) {
                            Text("Jugar de nuevo")
                        }
                    }
                }

                EstadoJuego.GameOver -> {
                    Column(
                        modifier = Modifier.size(width = anchoCarril * 3, height = altoArena),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Game Over",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = { reiniciar() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Controles
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { if (carrilJugador > 0) carrilJugador -= 1 },
                enabled = estado == EstadoJuego.Jugando
            ) {
                Text("Izquierda")
            }
            Button(
                onClick = { if (carrilJugador < 2) carrilJugador += 1 },
                enabled = estado == EstadoJuego.Jugando
            ) {
                Text("Derecha")
            }
        }
    }
}