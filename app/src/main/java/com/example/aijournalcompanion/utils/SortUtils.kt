package com.example.aijournalcompanion.utils

class SortUtils {
    fun <T, R : Comparable<R>> bubbleSortBy(source: List<T>, selector: (T) -> R): List<T> {
        val a = source.toMutableList()
        val n = a.size
        var swapped: Boolean
        do {
            swapped = false
            for (i in 0 until n - 1) {
                if (selector(a[i]) > selector(a[i + 1])) {
                    val tmp = a[i]
                    a[i] = a[i + 1]
                    a[i + 1] = tmp
                    swapped = true
                }
            }
        } while (swapped)
        return a
    }

    fun <T, R : Comparable<R>> insertionSortBy(source: List<T>, selector: (T) -> R): List<T> {
        val a = source.toMutableList()
        for (i in 1 until a.size) {
            val key = a[i]
            var j = i - 1
            while (j >= 0 && selector(a[j]) > selector(key)) {
                a[j + 1] = a[j]
                j--
            }
            a[j + 1] = key
        }
        return a
    }

    fun <T, R : Comparable<R>> selectionSortBy(source: List<T>, selector: (T) -> R): List<T> {
        val a = source.toMutableList()
        for (i in 0 until a.size - 1) {
            var minIdx = i
            for (j in i + 1 until a.size) {
                if (selector(a[j]) < selector(a[minIdx])) minIdx = j
            }
            if (minIdx != i) {
                val tmp = a[i]
                a[i] = a[minIdx]
                a[minIdx] = tmp
            }
        }
        return a
    }
}
