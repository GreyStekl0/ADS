package labs.lab1

import kotlin.random.Random

fun increasingSequenceInt(arr: IntArray): IntArray {
    for (i in 0 until arr.size) {
        arr[i] = i
    }
    return arr
}

fun decreasingSequenceInt(arr: IntArray): IntArray {
    var j = 0
    for (i in arr.size - 1 downTo 0) {
        arr[j] = i
        j++
    }
    return arr
}

fun randomSequenceInt(
    arr: IntArray,
    max: Int = 100,
    min: Int = 0,
): IntArray {
    for (i in 0 until arr.size) {
        arr[i] = Random.nextInt(min, max)
    }
    return arr
}

fun sawtoothSequenceInt(
    arr: IntArray,
    interval: Int = 10,
    min: Int = 0,
): IntArray {
    var j = min
    for (i in 0 until arr.size) {
        arr[i] = j
        j++
        if (j > interval - 1) {
            j = min
        }
    }
    return arr
}

fun sinusoidalSequenceInt(
    arr: IntArray,
    max: Int = 10,
    min: Int = 0,
): IntArray {
    val size = arr.size
    var j = max
    var isCoursent = false

    for (i in 0 until size) {
        arr[i] = j

        if (isCoursent) {
            j++
        } else {
            j--
        }

        if (j == max) {
            isCoursent = false
        } else if (j == min) {
            isCoursent = true
        }
    }
    return arr
}

fun staggeredSequenceInt(
    arr: IntArray,
    interval: Int = 5,
    delta: Int = 5,
    start: Int = 5,
): IntArray {
    val size = arr.size
    var min = start
    var max = start + delta
    var j = 0

    for (i in 0 until size) {
        arr[i] = Random.nextInt(min, max)
        j++
        if (j == interval) {
            min += delta
            max += delta
            j = 0
        }
    }

    return arr
}

fun quasiOrderedSequenceInt(
    arr: IntArray,
    start: Int = 10,
    delta: Int = 5,
): IntArray {
    val size = arr.size
    var min = start - delta
    var max = start + delta

    for (i in 0 until size) {
        arr[i] = Random.nextInt(min, max)
        min += delta
        max += delta
    }
    return arr
}

fun main() {
    val size = readln().toInt()
    val arr = IntArray(size)
    println(increasingSequenceInt(arr).joinToString())
    println(decreasingSequenceInt(arr).joinToString())
    println(randomSequenceInt(arr).joinToString())
    println(sawtoothSequenceInt(arr).joinToString())
    println(sinusoidalSequenceInt(arr).joinToString())
    println(staggeredSequenceInt(arr).joinToString())
    println(quasiOrderedSequenceInt(arr).joinToString())
}
