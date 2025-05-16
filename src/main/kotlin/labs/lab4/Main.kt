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

    for (i in 1..10) {
        val pSize = i
        println("--- Pattern size: $pSize ---\n")
        println("--- KMP Search Performance ---")
        for (i in 1000..10000 step 1000) {
            val chars = CharArray(i)
            fill(chars)
            val text = String(chars)

            val p = CharArray(pSize)
            fill(p)
            val pattern = String(p)

            repeat(warmupRuns) {
                kmpSearch(text, pattern)
            }

            var totalExecutionTimeNanoKmp = 0L
            repeat(measurementRuns) {
                totalExecutionTimeNanoKmp += measureNanoTime { kmpSearch(text, pattern) }
            }
            val averageTimeMsKmp = totalExecutionTimeNanoKmp / measurementRuns
            println(
                "KMP: Average execution time for text size $i: $averageTimeMsKmp ns",
            )
        }
        println("------------------------------\n")

        println("--- String.indexOf Performance ---")
        for (i in 1000..10000 step 1000) {
            val chars = CharArray(i)
            fill(chars)
            val text = String(chars)

            val p = CharArray(pSize)
            fill(p)
            val pattern = String(p)

            repeat(warmupRuns) {
                text.indexOf(pattern)
            }

            var totalExecutionTimeNanoIndexOf = 0L
            repeat(measurementRuns) {
                totalExecutionTimeNanoIndexOf += measureNanoTime { text.indexOf(pattern) }
            }
            val averageTimeMsIndexOf = totalExecutionTimeNanoIndexOf / measurementRuns
            println(
                "indexOf: Average execution time for text size $i: $averageTimeMsIndexOf ns",
            )
        }
        println("------------------------------\n")
    }
}
