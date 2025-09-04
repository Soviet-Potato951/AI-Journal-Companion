package com.example.aijournalcompanion.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aijournalcompanion.model.EmotionEntry
import com.example.aijournalcompanion.model.SortMethod
import com.example.aijournalcompanion.utils.SortUtils

@Composable
fun SortSectionApply(
    emotionHistory: List<EmotionEntry>,
    onApply: (List<EmotionEntry>) -> Unit,
    modifier: Modifier = Modifier
) {
    var sortMethod by rememberSaveable { mutableStateOf(SortMethod.INSERTION) }
    var expanded by remember { mutableStateOf(false) }
    val sortUtils = remember { SortUtils() }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text("Sort: ${sortMethod.name}")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                SortMethod.entries.forEach { m ->
                    DropdownMenuItem(
                        text = { Text(m.name) },
                        onClick = { sortMethod = m; expanded = false }
                    )
                }
            }
        }

        Spacer(Modifier.width(8.dp))

        Button(
            enabled = emotionHistory.isNotEmpty(),
            onClick = {
                val sorted: List<EmotionEntry> = when (sortMethod) {
                    SortMethod.BUBBLE ->
                        sortUtils.bubbleSortBy(emotionHistory) { e: EmotionEntry -> e.emotion.uppercase() }
                    SortMethod.INSERTION ->
                        sortUtils.insertionSortBy(emotionHistory) { e: EmotionEntry -> e.emotion.uppercase() }
                    SortMethod.SELECTION ->
                        sortUtils.selectionSortBy(emotionHistory) { e: EmotionEntry -> e.emotion.uppercase() }
                }
                onApply(sorted)
            }
        ) { Text("Apply Sort") }
    }
}
