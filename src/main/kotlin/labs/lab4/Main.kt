package labs.lab4

import kotlin.random.Random
import kotlin.system.measureNanoTime

fun computeLPSArray(pattern: String): IntArray {
    val m = pattern.length
    val lps = IntArray(m)
    var length = 0
    var i = 1
    lps[0] = 0

    while (i < m) {
        if (pattern[i] == pattern[length]) {
            length++
            lps[i] = length
            i++
        } else {
            if (length != 0) {
                length = lps[length - 1]
            } else {
                lps[i] = 0
                i++
            }
        }
    }
    return lps
}

fun kmpSearch(
    text: String,
    pattern: String,
): Int {
    val n = text.length
    val m = pattern.length
    val lps = computeLPSArray(pattern)

    var i = 0
    var j = 0
    while (i < n) {
        if (pattern[j] == text[i]) {
            i++
            j++
        }
        if (j == m) {
            return i - j
        } else if (i < n && pattern[j] != text[i]) {
            if (j != 0) {
                j = lps[j - 1]
            } else {
                i++
            }
        }
    }
    return -1
}

fun fill(arr: CharArray) {
    for (i in arr.indices) {
        val vs = listOf('a', 'b', 'c', 'd', 'e', 'f')
        arr[i] = vs[Random.nextInt(0, vs.size)]
    }
}

fun main() {
    val warmupRuns = 5
    val measurementRuns = 10
    val kmpResult = hashMapOf<Int, MutableList<Long>>()
    val indexOfResult = hashMapOf<Int, MutableList<Long>>()
    val textSizes = (1000..10000 step 1000).toList()

    for (i in 1..10) {
        val pSize = i
        kmpResult[i] = mutableListOf()
        indexOfResult[i] = mutableListOf()
        for (j in textSizes) {
            val p = CharArray(pSize)
            fill(p)
            val pattern = String(p)
            val chars = CharArray(j)
            fill(chars)
            val text = String(chars)

            repeat(warmupRuns) {
                kmpSearch(text, pattern)
            }

            var totalExecutionTimeNanoKmp = 0L
            repeat(measurementRuns) {
                totalExecutionTimeNanoKmp += measureNanoTime { kmpSearch(text, pattern) }
            }
            val averageTimeNanoKmp = totalExecutionTimeNanoKmp / measurementRuns
            kmpResult[i]?.add(averageTimeNanoKmp)

            repeat(warmupRuns) {
                text.indexOf(pattern)
            }

            var totalExecutionTimeNanoIndexOf = 0L
            repeat(measurementRuns) {
                totalExecutionTimeNanoIndexOf += measureNanoTime { text.indexOf(pattern) }
            }
            val averageTimeNanoIndexOf = totalExecutionTimeNanoIndexOf / measurementRuns
            indexOfResult[i]?.add(averageTimeNanoIndexOf)
        }
    }

    println("Результаты измерений (среднее время в наносекундах):")
    println("----------------------------------------------------")
    println("Размер текста: ${textSizes.joinToString(" ") { String.format("%7d", it) }}")
    println("----------------------------------------------------")

    for (pSize in kmpResult.keys.sorted()) {
        println("Размер шаблона: $pSize")
        println("  KMP       : ${kmpResult[pSize]?.joinToString("") { String.format("%8d", it) }}")
        println("  IndexOf   : ${indexOfResult[pSize]?.joinToString("") { String.format("%8d", it) }}")
        println("----------------------------------------------------")
    }
}
