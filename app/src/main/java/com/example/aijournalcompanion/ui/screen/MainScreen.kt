package com.example.aijournalcompanion.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aijournalcompanion.api.AnalysisResponse
import com.example.aijournalcompanion.model.EmotionEntry
import com.example.aijournalcompanion.model.toEmotions
import com.example.aijournalcompanion.ui.components.AnalyzeBar
import com.example.aijournalcompanion.ui.components.SearchSection
import com.example.aijournalcompanion.ui.chart.DragAndDrop.PastEmotions
import com.example.aijournalcompanion.ui.components.HelpDialog
import com.example.aijournalcompanion.ui.components.SortSectionApply

import com.example.aijournalcompanion.ui.chart.PieChart  // add this import

@Composable
fun MainScreen() {
    var emotionHistory by remember { mutableStateOf(listOf<EmotionEntry>()) }
    var lastResponse by remember { mutableStateOf<AnalysisResponse?>(null) }
    var listVersion by remember { mutableIntStateOf(0) }

    // NEW: state to control the chart dialog
    var showPieChart by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AnalyzeBar { res: AnalysisResponse, _: String ->
            lastResponse = res
            val entry = EmotionEntry(
                id = System.currentTimeMillis(),
                emotion = res.emotion,
                advice = res.advice,
                emoji = res.emoji
            )
            emotionHistory = emotionHistory + entry
        }

        Spacer(Modifier.height(12.dp))
        lastResponse?.let { r ->
            Text("AI Response", style = MaterialTheme.typography.titleMedium)
            Text("Emotion: ${r.emotion}")
            Text("Advice: ${r.advice}")
            Text("Emoji: ${r.emoji}")
            Spacer(Modifier.height(8.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SortSectionApply(
                emotionHistory = emotionHistory,
                onApply = { sorted ->
                    emotionHistory = sorted
                    listVersion++
                },
                modifier = Modifier.weight(1f)
            )

            // NEW: button to open the pie chart
            Button(onClick = { showPieChart = true }) {
                Text("Show Chart")
            }
        }

        Spacer(Modifier.height(16.dp))
        SearchSection(
            emotionHistory = emotionHistory, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Text("Journal History", style = MaterialTheme.typography.titleMedium)
        PastEmotions(
            history = emotionHistory,
            listVersion = listVersion,
            onDelete = { id -> emotionHistory = emotionHistory.filterNot { it.id == id } },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        HelpDialog()
    }

    PieChart.EmotionPieChartDialog(
        show = showPieChart,
        onDismiss = { },
        emotions = emotionHistory.toEmotions()
    )

}
