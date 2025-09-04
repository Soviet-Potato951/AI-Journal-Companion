package com.example.aijournalcompanion.model


data class Emotion(val category: String, val amount: Int, val name: String)
data class EmotionEntry(val id: Long, val emotion: String, val advice: String, val emoji: String)
enum class SortMethod { BUBBLE, INSERTION, SELECTION }
enum class SearchMethod { BST, HASHMAP, DOUBLY_LINKED_LIST }

fun List<EmotionEntry>.toEmotions(): List<Emotion> {
    return this
        .groupBy { it.emotion }
        .map { (emotionStr, entries) ->
            Emotion(
                category = emotionStr,
                name = emotionStr,
                amount = entries.size
            )
        }
        .sortedByDescending { it.amount }
}
