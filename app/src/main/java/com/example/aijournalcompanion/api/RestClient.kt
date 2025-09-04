// RestClient.kt
package com.example.aijournalcompanion.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import kotlin.coroutines.cancellation.CancellationException
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

data class AiResult(val advice: String, val emotion: String, val emoji: String)

object RestClient {
    private val gson = Gson()

    @Volatile
    private var baseUrl: String = "http://10.0.2.2:8000/"

    suspend fun analyze(text: String): Result<AiResult> = withContext(Dispatchers.IO) {
        runCatching { analyzeBlocking(text) }
    }

    @Throws(Exception::class)
    private fun analyzeBlocking(text: String): AiResult {

        val url = URL("${baseUrl}analyse")
        val conn = (url.openConnection() as HttpURLConnection)

        try {
            // --- Request setup ---
            conn.requestMethod = "POST"
            conn.instanceFollowRedirects = true
            conn.useCaches = false
            conn.doInput = true
            conn.doOutput = true
            conn.connectTimeout = 10_000
            conn.readTimeout = 30_000
            conn.setRequestProperty("Accept", "application/json")
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")

            // Body
            val body = gson.toJson(mapOf("text" to text))
            val bytes = body.toByteArray(Charsets.UTF_8)
            // Tell HUC the exact length to avoid buffering/copy
            conn.setFixedLengthStreamingMode(bytes.size)

            // Write
            conn.outputStream.use { os ->
                os.write(bytes)
                os.flush()
            }

            // Read
            val code = conn.responseCode
            val stream: InputStream? = if (code in 200..299) conn.inputStream else conn.errorStream
            val payload = stream?.use { it.readTextFully() }.orEmpty()

            if (code !in 200..299) {
                // Try to show FastAPI's {"detail": "..."} if present
                val detail = runCatching {
                    gson.fromJson(payload, JsonObject::class.java)?.get("detail")?.asString
                }.getOrNull()
                val msg = detail ?: payload.ifBlank { "HTTP $code error with empty body" }
                throw IllegalStateException(msg)
            }

            // Parse the JSON response from your FastAPI
            val obj: JsonObject = gson.fromJson(payload, JsonObject::class.java) ?: JsonObject()
            val advice = obj.get("advice")?.asString ?: "(No advice)"
            val emotion = obj.get("emotion")?.asString ?: "NEUTRAL"
            val emoji = obj.get("emoji")?.asString ?: "😐"

            return AiResult(advice = advice, emotion = emotion, emoji = emoji)
        } catch (ce: CancellationException) {
            // If the coroutine is cancelled, try to cut the socket quickly
            throw ce
        } finally {
            // This helps terminate the underlying socket sooner
            try { conn.disconnect() } catch (_: Throwable) {}
        }
    }


    // --- Helpers ---
    private fun InputStream.readTextFully(): String =
        BufferedReader(InputStreamReader(this, Charsets.UTF_8)).use { it.readText() }

}
