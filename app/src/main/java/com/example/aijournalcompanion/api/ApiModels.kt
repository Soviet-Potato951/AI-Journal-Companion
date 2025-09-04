package com.example.aijournalcompanion.api

import com.google.gson.annotations.SerializedName

data class AnalysisRequest(
    @SerializedName("text")
    val text: String
)

data class AnalysisResponse(
    @SerializedName("emotion")
    val emotion: String,

    @SerializedName("advice")
    val advice: String,

    @SerializedName("emoji")
    val emoji: String
)