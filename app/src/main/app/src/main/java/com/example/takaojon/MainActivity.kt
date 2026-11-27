
package com.example.takaojon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF17345C)
private val Blue = Color(0xFF2563EB)
private val Green = Color(0xFF16834A)
private val Bg = Color(0xFFF3F6FA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TakaOjonApp() }
    }
}

@Composable
fun TakaOjonApp() {
    var rateText by remember { mutableStateOf("100") }
    var mode by remember { mutableStateOf("") }
    var input by remember { mutableStateOf("") }

    val rate = rateText.toDoubleOrNull() ?: 0.0
    val value = input.toDoubleOrNull() ?: 0.0
    val result = if (mode == "taka") value / rate else value * rate

    MaterialTheme {
        Surface(Modifier.fillMaxSize(), color = Bg) {
            Column(
                Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))

                Text(
                    "হিসাব ক্যালকুলেটর",
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy
                )

                Spacer(Modifier.height(24.dp))

                Card(
                    Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text(
                            "প্রতি কেজির দাম (টাকা)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Navy
                        )

                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = rateText,
                            onValueChange = { rateText = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = { Text("দাম লিখুন") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal
                            ),
                            suffix = { Text("৳ / কেজি") }
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Tile(
                        "টাকা", "টাকা থেকে ওজন",
                        Blue, Modifier.weight(1f)
                    ) {
                        mode = "taka"
                        input = ""
                    }

                    Tile(
                        "ওজন", "ওজন থেকে টাকা",
                        Green, Modifier.weight(1f)
                    ) {
                        mode = "ojon"
                        input = ""
                    }
                }

                if (mode.isNotEmpty()) {
                    Spacer(Modifier.height(24.dp))

                    Card(
                        Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(Modifier.padding(18.dp)) {
                            Text(
                                if (mode == "taka")
                                    "টাকার পরিমাণ লিখুন"
                                else "ওজন লিখুন (কেজি)",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = Navy
                            )

                            Spacer(Modifier.height(8.dp))

                            OutlinedTextField(
                                value = input,
                                onValueChange = { input = it },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                placeholder = {
                                    Text(
                                        if (mode == "taka")
                                            "যেমন: ৫০"
                                        else "যেমন: ১.৫"
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal
                                ),
                                suffix = {
                                    Text(
                                        if (mode == "taka")
                                            "টাকা"
                                        else "কেজি"
                                    )
                                }
                            )

                            Spacer(Modifier.height(16.dp))

                            Text(
                                if (mode == "taka")
                                    "হিসাব করা ওজন"
                                else "মোট দাম",
                                color = Color.DarkGray
                            )

                            Spacer(Modifier.height(6.dp))

                            val answer = when {
                                input.isBlank() || rate <= 0 -> "—"

                                mode == "taka" -> {
                                    val kg = result.toLong()
                                    val grams =
                                        ((result - kg) * 1000).toInt()
                                    "$kg কেজি $grams গ্রাম"
                                }

                                else -> "৳ ${fmt(result)}"
                            }

                            Text(
                                answer,
                                Modifier.fillMaxWidth(),
                                fontSize = 27.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (mode == "taka") Blue else Green,
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(12.dp))

                            OutlinedButton(
                                onClick = { input = "" },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("মুছে ফেলুন")
                            }
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))

                Text(
                    "ইন্টারনেট ছাড়াই ব্যবহার করুন",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun Tile(
    title: String,
    subtitle: String,
    color: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier.height(142.dp)
            .background(color, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            title,
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(7.dp))

        Text(
            subtitle,
            color = Color.White,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

fun fmt(v: Double) =
    if (v % 1.0 == 0.0)
        v.toLong().toString()
    else String.format("%.2f", v)
