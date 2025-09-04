package com.example.aijournalcompanion.utils

import com.example.aijournalcompanion.model.EmotionEntry
import com.example.aijournalcompanion.model.SearchMethod

object SearchUtils {

    fun search(
        method: SearchMethod,
        entries: List<EmotionEntry>,
        queryEmotion: String
    ): List<EmotionEntry> {
        if (queryEmotion.isBlank() || entries.isEmpty()) return emptyList()
        val key = queryEmotion.uppercase()

        return when (method) {
            SearchMethod.BST -> bstSearch(entries, key)
            SearchMethod.HASHMAP -> hashMapSearch(entries, key)
            SearchMethod.DOUBLY_LINKED_LIST -> dllSearch(entries, key)
        }
    }
    private data class Node(
        val key: String,
        val values: MutableList<EmotionEntry> = mutableListOf(),
        var left: Node? = null,
        var right: Node? = null
    )

    private fun bstSearch(entries: List<EmotionEntry>, key: String): List<EmotionEntry> {
        var root: Node? = null

        fun insert(cur: Node?, k: String, e: EmotionEntry): Node {
            if (cur == null) return Node(k, mutableListOf(e))
            when {
                k < cur.key -> cur.left = insert(cur.left, k, e)
                k > cur.key -> cur.right = insert(cur.right, k, e)
                else -> cur.values.add(e)
            }
            return cur
        }

        entries.forEach { entry ->
            val k = entry.emotion.uppercase()
            root = insert(root, k, entry)
        }

        var cur = root
        while (cur != null) {
            cur = when {
                key < cur.key -> cur.left
                key > cur.key -> cur.right
                else -> return cur.values.toList()
            }
        }
        return emptyList()
    }

    private fun hashMapSearch(entries: List<EmotionEntry>, key: String): List<EmotionEntry> {
        val map = HashMap<String, MutableList<EmotionEntry>>()
        for (e in entries) {
            val k = e.emotion.uppercase()
            val bucket = map.getOrPut(k) { mutableListOf() }
            bucket.add(e)
        }
        return map[key]?.toList() ?: emptyList()
    }

    // --------------------------------
    // 3) Doubly Linked List (linear scan)
    // --------------------------------
    private fun dllSearch(entries: List<EmotionEntry>, key: String): List<EmotionEntry> {
        // Simplest interpretation: a linear walk like a DLL traversal.
        // We don't actually need to build pointers to demonstrate the algorithmic approach.
        val out = ArrayList<EmotionEntry>()
        for (e in entries) {
            if (e.emotion.equals(key, ignoreCase = true)) out.add(e)
        }
        return out
    }
}
