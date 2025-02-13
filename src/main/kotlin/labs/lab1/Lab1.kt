package labs.lab1

import kotlin.random.Random

inline fun <reified T : Number> increasingSequence(arr: Array<T>): Array<T> {
    for (i in arr.indices) {
        arr[i] =
            when (T::class) {
                Int::class -> i as T
                Double::class -> i.toDouble() as T
                else -> throw IllegalArgumentException("Unsupported type")
            }
    }
    return arr
}

inline fun <reified T : Number> decreasingSequence(arr: Array<T>): Array<T> {
    var j = 0
    for (i in arr.size - 1 downTo 0) {
        arr[j] =
            when (T::class) {
                Int::class -> i as T
                Double::class -> i.toDouble() as T
                else -> throw IllegalArgumentException("Unsupported type")
            }
        j++
    }
    return arr
}

inline fun <reified T : Number> randomSequence(
    arr: Array<T>,
    max: T,
    min: T,
): Array<T> {
    for (i in arr.indices) {
        arr[i] =
            when (T::class) {
                Int::class -> Random.nextInt(min.toInt(), max.toInt()) as T
                Double::class -> Random.nextDouble(min.toDouble(), max.toDouble()) as T
                else -> throw IllegalArgumentException("Unsupported type")
            }
    }
    return arr
}

inline fun <reified T : Number> sawtoothSequence(
    arr: Array<T>,
    interval: Int,
    min: T,
): Array<T> {
    var j = min.toInt()
    for (i in arr.indices) {
        arr[i] =
            when (T::class) {
                Int::class -> j as T
                Double::class -> j.toDouble() as T
                else -> throw IllegalArgumentException("Unsupported type")
            }
        j++
        if (j > interval - 1) {
            j = min.toInt()
        }
    }
    return arr
}

inline fun <reified T : Number> sinusoidalSequence(
    arr: Array<T>,
    max: T,
    min: T,
): Array<T> {
    var j = max.toInt()
    var isCoursent = false

    for (i in arr.indices) {
        arr[i] =
            when (T::class) {
                Int::class -> j as T
                Double::class -> j.toDouble() as T
                else -> throw IllegalArgumentException("Unsupported type")
            }

        if (isCoursent) {
            j++
        } else {
            j--
        }

        if (j == max.toInt()) {
            isCoursent = false
        } else if (j == min.toInt()) {
            isCoursent = true
        }
    }
    return arr
}

inline fun <reified T : Number> staggeredSequence(
    arr: Array<T>,
    interval: Int,
    delta: T,
    start: T,
): Array<T> {
    var min = start.toInt()
    var max = start.toInt() + delta.toInt()
    var j = 0

    for (i in arr.indices) {
        arr[i] =
            when (T::class) {
                Int::class -> Random.nextInt(min, max) as T
                Double::class -> Random.nextDouble(min.toDouble(), max.toDouble()) as T
                else -> throw IllegalArgumentException("Unsupported type")
            }
        j++
        if (j == interval) {
            min += delta.toInt()
            max += delta.toInt()
            j = 0
        }
    }

    return arr
}

inline fun <reified T : Number> quasiOrderedSequence(
    arr: Array<T>,
    start: T,
    delta: T,
): Array<T> {
    var min = start.toInt() - delta.toInt()
    var max = start.toInt() + delta.toInt()

    for (i in arr.indices) {
        arr[i] =
            when (T::class) {
                Int::class -> Random.nextInt(min, max) as T
                Double::class -> Random.nextDouble(min.toDouble(), max.toDouble()) as T
                else -> throw IllegalArgumentException("Unsupported type")
            }
        min += delta.toInt()
        max += delta.toInt()
    }
    return arr
}

fun main() {
    print("Введите размер массива: ")
    val size = readln().toInt()
    val min = 0
    val max = 100
    val interval = 10
    val delta = 10
    val start = 0
    val arr = Array(size) { 0 }

    println(increasingSequence(arr).joinToString())
    println(decreasingSequence(arr).joinToString())
    println(randomSequence(arr, max, min).joinToString())
    println(sawtoothSequence(arr, interval, min).joinToString())
    println(sinusoidalSequence(arr, max, min).joinToString())
    println(staggeredSequence(arr, interval, delta, start).joinToString())
    println(quasiOrderedSequence(arr, start, delta).joinToString())
}
