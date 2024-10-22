package com.example.di_calculadora_jp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.benchmark.perfetto.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.di_calculadora_jp.ui.theme.DI_Calculadora_JPTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import kotlin.math.floor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DI_Calculadora_JPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun calculadoraLoca() {
    // Variable para almacenar la expresión actual
    var expresion by remember { mutableStateOf("") }
    // Variable para almacenar el resultado actual
    var result by remember { mutableStateOf("") }

    // Función para evaluar la expresión básica (+, -, *, /)
    fun evalExpression(expr: String): Double {
        return try {
            // Dividimos la expresión en tokens (números y operadores)
            val tokens = expr.split(" ")
            // Inicializamos el resultado con el primer número
            var resultado = tokens[0].toDouble()
            var operador = ""
            for (i in 1 until tokens.size) {
                if (i % 2 == 0) {
                    when (operador) {
                        "+" -> resultado += tokens[i].toDouble()
                        "-" -> resultado -= tokens[i].toDouble()
                        "*" -> resultado *= tokens[i].toDouble()
                        "/" -> resultado /= tokens[i].toDouble()
                    }
                } else {
                    operador = tokens[i]
                }
            }
            resultado
        } catch (e: Exception) {
            // En caso de error, devolvemos 0.0
            0.0
        }
    }

    // Función para formatear el resultado: Si es un entero, eliminar los decimales ".0"
    fun formatResult(result: Double): String {
        return if (result == floor(result)) {
            // Si es un número entero, lo mostramos sin decimales
            result.toInt().toString()
        } else {
            // Si tiene decimales, mostramos el número completo
            result.toString()
        }
    }

    // Función para remplazar el 5 por el 6
    fun evaluateExpression(expr: String): String {
        return try {
            // Evaluamos la expresión
            val evaluated = evalExpression(expr)
            // Formateamos el resultado
            val formattedResult = formatResult(evaluated)
            // Reemplazamos el dígito '5' por '6' en el resultado, si existe
            if (formattedResult.contains("5")) formattedResult.replace("5", "6") else formattedResult
        } catch (e: Exception) {
            "Error"
        }
    }
    // Diseño de la calculadora
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .systemBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mostrar el texto, la expresión actual
        Text(
            text = "Expresión: $expresion",
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Color(0xFF1F1F1F), shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        )
        // Mostrar el resultado
        Text(
            text = "Resultado: $result",
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Color(0xFF1F1F1F), shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        )
        // Filas de botones
        val buttons = listOf(
            listOf("0", "1", "2", "/"),
            listOf("3", "4", "6", "*"),
            listOf("7", "8", "9", "-"),
            listOf("C", "=", "+")
        )
        // Bucle para recorrer las filas de botones
        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { label ->
                    Button(
                        onClick = {
                            when (label) {
                                "=" -> {
                                    // Evaluamos la expresión y actualiza el resultado
                                    result = evaluateExpression(expresion)
                                    // Actualiza la expresión con el resultado
                                    expresion = result
                                }
                                // Borramos la expresión y el resultado
                                "C" -> {
                                    expresion = ""
                                    result = ""
                                }
                                "+" -> {
                                    expresion += " + "
                                }
                                "-" -> {
                                    expresion += " - "
                                }
                                "*" -> {
                                    expresion += " * "
                                }
                                "/" -> {
                                    expresion += " / "
                                }
                                else -> {
                                    // Le asignamos el valor que va a tener cada número
                                    val nuevoValor = when (label) {
                                        "3" -> "6"
                                        "2" -> "4"
                                        "4" -> "6"
                                        "1" -> "3"
                                        "6" -> "8"
                                        "0" -> "2"
                                        "8" -> "0"
                                        "7" -> "9"
                                        "9" -> "1"
                                        else -> label
                                    }
                                    //Lo añadimos a la expresion
                                    expresion += nuevoValor
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .size(80.dp)
                            .background(Color(0xFF6200EE), shape = RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(
                            // Color del botón
                            containerColor = Color(0xFF6200EE),
                            // Colo del texto
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = label, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    calculadoraLoca()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DI_Calculadora_JPTheme {
        Greeting("Android")
    }
}