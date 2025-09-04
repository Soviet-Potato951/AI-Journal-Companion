package com.example.aijournalcompanion.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aijournalcompanion.model.EmotionEntry
import com.example.aijournalcompanion.model.SearchMethod

@Composable
fun SearchSection(
    emotionHistory: List<EmotionEntry>,
    modifier: Modifier = Modifier
) {
    var searchMethod by rememberSaveable { mutableStateOf(SearchMethod.HASHMAP) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<EmotionEntry>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) } // for dropdown

    Column(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // --- Search method dropdown ---
            Box {
                OutlinedButton(onClick = { expanded = true }) {
                    Text("Search: ${searchMethod.name}")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    SearchMethod.entries.forEach { m ->
                        DropdownMenuItem(
                            text = { Text(m.name) },
                            onClick = {
                                searchMethod = m
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.width(8.dp))

            // --- Query input ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Emotion") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(8.dp))

            Button(
                enabled = searchQuery.isNotBlank(),
                onClick = {
                    searchResults = com.example.aijournalcompanion.utils.SearchUtils.search(
                        method = searchMethod,
                        entries = emotionHistory,
                        queryEmotion = searchQuery.trim()
                    )
                }
            ) { Text("Search") }
        }

        Spacer(Modifier.height(12.dp))

        // --- Results ---
        Text("Search Results", style = MaterialTheme.typography.titleMedium)
        if (searchResults.isEmpty()) {
            Text("No matches yet. Try JOY, SADNESS, ANGER, etc.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn {
                items(searchResults, key = { it.id }) { entry ->
                    Card(Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text(entry.emotion, style = MaterialTheme.typography.titleSmall)
                            Text(entry.advice)
                            Text(entry.emoji)
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}