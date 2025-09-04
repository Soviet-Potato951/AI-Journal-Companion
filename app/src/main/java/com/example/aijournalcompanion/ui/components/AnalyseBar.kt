package com.example.aijournalcompanion.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aijournalcompanion.api.AiResult
import com.example.aijournalcompanion.api.AnalysisResponse
import com.example.aijournalcompanion.api.RestClient
import kotlinx.coroutines.launch

private fun AiResult.toAnalysisResponse(): AnalysisResponse =
    AnalysisResponse(
        emotion = emotion,
        advice = advice,
        emoji = emoji
    )

@Composable
fun AnalyzeBar(
    onAnalyzed: (AnalysisResponse, String /*raw journal text*/) -> Unit
) {
    var input by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Enter a Journal Entry") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                error = null
                if (input.isBlank()) return@Button
                loading = true
                scope.launch {
                    val result = RestClient.analyze(input)
                    loading = false
                    result.onSuccess { res ->
                        onAnalyzed(res.toAnalysisResponse(), input)
                        input = ""
                    }.onFailure { t ->
                        error = t.message ?: "Something went wrong"
                    }
                }
            },
            enabled = !loading && input.isNotBlank()
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            } else {
                Text("Analyze")
            }
        }

        error?.let { Text("Error: $it", color = MaterialTheme.colorScheme.error) }
    }
}
