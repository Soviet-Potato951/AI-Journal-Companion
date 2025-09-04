package com.example.aijournalcompanion.ui.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.aijournalcompanion.model.Emotion
import com.example.aijournalcompanion.ui.components.EmotionLegend

object PieChart {
    @Composable
    fun EmotionPieChartDialog(show: Boolean, onDismiss: () -> Unit, emotions: List<Emotion>) {
        if (!show) return
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Emotion Frequency") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    EmotionPieChart(emotions = emotions, height = 260.dp)
                    EmotionLegend(emotions = emotions)
                }
            },
            confirmButton = { TextButton(onClick = onDismiss) { Text("OK") } }
        )
    }

    @Composable
    fun EmotionPieChart(emotions: List<Emotion>, height: Dp = 260.dp) {
        val total = emotions.sumOf { it.amount.toDouble() }.toFloat().takeIf { it != 0f } ?: 1f

        val sliceColors = listOf(
            Color(0xFFFF1744), // red (A400)
            Color(0xFFFF9100), // orange (A700)
            Color(0xFFFFEA00), // yellow (A400)
            Color(0xFF00E676), // green (A400)
            Color(0xFF2979FF), // blue (A400)
            Color(0xFF651FFF), // indigo (A400)
            Color(0xFFD500F9), // violet (A400)
        )

        Canvas(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .height(height)
                .padding(16.dp)
        ) {
            var startAngle = -90f // 12 o’clock
            val diameter = size.minDimension
            val pieSize = Size(diameter, diameter)
            val pieOffset = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)

            emotions.forEachIndexed { index, slice ->
                val sweepAngle = (slice.amount / total) * 360f
                drawArc(
                    color = sliceColors[index % sliceColors.size],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = pieOffset,
                    size = pieSize
                )
                startAngle += sweepAngle
            }
        }
    }
}