package com.example.aijournalcompanion.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.aijournalcompanion.model.Emotion

@Composable
fun EmotionLegend(emotions: List<Emotion>) {
    val colors = listOf(
        Color(0xFFFF1744), // red (A400)
        Color(0xFFFF9100), // orange (A700)
        Color(0xFFFFEA00), // yellow (A400)
        Color(0xFF00E676), // green (A400)
        Color(0xFF2979FF), // blue (A400)
        Color(0xFF651FFF), // indigo (A400)
        Color(0xFFD500F9), // violet (A400)
    )
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        val total = emotions.sumOf { it.amount.toDouble() }
        emotions.forEachIndexed { i, e ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Canvas(modifier = Modifier.size(12.dp)) { drawCircle(colors[i % colors.size]) }
                val pct = if (total <= 0.0) 0.0 else (e.amount.toDouble() / total) * 100.0
                Text("${e.category}: ${"%.1f".format(pct)}%")
            }
        }
    }
}