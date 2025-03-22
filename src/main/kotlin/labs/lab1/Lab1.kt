package labs.lab1

import kotlin.random.Random

inline fun <reified T : Number> increasingSequence(arr: Array<T>): Array<T> {
    when (T::class) {
        Int::class -> {
            for (i in arr.indices) {
                arr[i] = i as T
            }
        }

        Double::class -> {
            for (i in arr.indices) {
                arr[i] = i.toDouble() as T
            }
        }

        else -> throw IllegalArgumentException("Unsupported type")
    }
    return arr
}

inline fun <reified T : Number> decreasingSequence(arr: Array<T>): Array<T> {
    when (T::class) {
        Int::class -> {
            for (i in arr.indices) {
                arr[i] = (arr.size - 1 - i) as T
            }
        }

        Double::class -> {
            for (i in arr.indices) {
                arr[i] = (arr.size - 1 - i).toDouble() as T
            }
        }

        else -> throw IllegalArgumentException("Unsupported type")
    }
    return arr
}

inline fun <reified T : Number> randomSequence(
    arr: Array<T>,
    max: T,
    min: T,
): Array<T> {
    val minInt = min.toInt()
    val maxInt = max.toInt()
    val minDouble = min.toDouble()
    val maxDouble = max.toDouble()

    when (T::class) {
        Int::class -> {
            for (i in arr.indices) {
                arr[i] = Random.nextInt(minInt, maxInt) as T
            }
        }

        Double::class -> {
            for (i in arr.indices) {
                arr[i] = Random.nextDouble(minDouble, maxDouble) as T
            }
        }

        else -> throw IllegalArgumentException("Unsupported type")
    }
    return arr
}

inline fun <reified T : Number> sawtoothSequence(
    arr: Array<T>,
    interval: Int,
    min: T,
): Array<T> {
    val minValue = min.toInt()

    when (T::class) {
        Int::class -> {
            for (i in arr.indices) {
                arr[i] = (minValue + (i % interval)) as T
            }
        }

        Double::class -> {
            for (i in arr.indices) {
                arr[i] = (minValue + (i % interval)).toDouble() as T
            }
        }

        else -> throw IllegalArgumentException("Unsupported type")
    }
    return arr
}

inline fun <reified T : Number> sinusoidalSequence(
    arr: Array<T>,
    max: T,
    min: T,
): Array<T> {
    val maxValue = max.toInt()
    val minValue = min.toInt()
    val range = maxValue - minValue
    val period = 2 * range

    when (T::class) {
        Int::class -> {
            for (i in arr.indices) {
                val position = i % period
                val value =
                    if (position < range) {
                        minValue + position
                    } else {
                        maxValue - (position - range)
                    }
                arr[i] = value as T
            }
        }

        Double::class -> {
            for (i in arr.indices) {
                val position = i % period
                val value =
                    if (position < range) {
                        minValue + position
                    } else {
                        maxValue - (position - range)
                    }
                arr[i] = value.toDouble() as T
            }
        }

        else -> throw IllegalArgumentException("Unsupported type")
    }
    return arr
}

inline fun <reified T : Number> staggeredSequence(
    arr: Array<T>,
    interval: Int,
    delta: T,
    start: T,
): Array<T> {
    val startValue = start.toInt()
    val deltaValue = delta.toInt()

    when (T::class) {
        Int::class -> {
            for (i in arr.indices) {
                val intervalIndex = i / interval
                val min = startValue + intervalIndex * deltaValue
                val max = min + deltaValue
                arr[i] = Random.nextInt(min, max) as T
            }
        }

        Double::class -> {
            for (i in arr.indices) {
                val intervalIndex = i / interval
                val min = startValue + intervalIndex * deltaValue
                val max = min + deltaValue
                arr[i] = Random.nextDouble(min.toDouble(), max.toDouble()) as T
            }
        }

        else -> throw IllegalArgumentException("Unsupported type")
    }
    return arr
}

inline fun <reified T : Number> quasiOrderedSequence(
    arr: Array<T>,
    start: T,
    delta: T,
): Array<T> {
    val startValue = start.toInt()
    val deltaValue = delta.toInt()

    when (T::class) {
        Int::class -> {
            for (i in arr.indices) {
                val baseValue = startValue + i * deltaValue
                val min = baseValue - deltaValue
                val max = baseValue + deltaValue
                arr[i] = Random.nextInt(min, max) as T
            }
        }

        Double::class -> {
            for (i in arr.indices) {
                val baseValue = startValue + i * deltaValue
                val min = baseValue - deltaValue
                val max = baseValue + deltaValue
                arr[i] = Random.nextDouble(min.toDouble(), max.toDouble()) as T
            }
        }

        else -> throw IllegalArgumentException("Unsupported type")
    }
    return arr
}

fun main() {
    print("Введите размер массива: ")
    val size = readln().toInt()
    val min = 0
    val max = 10
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
