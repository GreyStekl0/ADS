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

fun measureAverageExecutionTime(
    iterations: Int,
    function: () -> Unit,
): Double {
    var totalTime = 0L
    repeat(iterations) {
        val startTime = System.nanoTime()
        function()
        val endTime = System.nanoTime()
        totalTime += (endTime - startTime)
    }
    return totalTime.toDouble() / iterations / 1_000_000 // Convert to milliseconds
}

fun main() {
    print("Введите размер массива: ")
    val size = readln().toInt()
    val iterations = 5

    // Warm up the JVM
    println("Warming up JVM...")
    repeat(10) {
        val warmupArrInt = Array<Int>(size) { 0 }
        val warmupArrDouble = Array<Double>(size) { 0.0 }
        increasingSequence(warmupArrInt)
        increasingSequence(warmupArrDouble)
    }

    // Test with Int arrays
    println("\nИзмерение для Int arrays:")
    testWithIntArrays(size, iterations)

    // Test with Double arrays
    println("\nИзмерение для Double arrays:")
    testWithDoubleArrays(size, iterations)
}

fun testWithIntArrays(
    size: Int,
    iterations: Int,
) {
    val min = 0
    val max = 100
    val interval = 10
    val delta = 10
    val start = 0

    // Your existing benchmark code for Int arrays
    val increasingTime =
        measureAverageExecutionTime(iterations) {
            val arr = Array(size) { 0 }
            increasingSequence(arr)
        }
    println("Increasing sequence: $increasingTime мс")

    // Decreasing sequence
    val decreasingTime =
        measureAverageExecutionTime(iterations) {
            val arr = Array(size) { 0 }
            decreasingSequence(arr)
        }
    println("Decreasing sequence: $decreasingTime мс")

    // Random sequence
    val randomTime =
        measureAverageExecutionTime(iterations) {
            val arr = Array(size) { 0 }
            randomSequence(arr, max, min)
        }
    println("Random sequence: $randomTime мс")

    // Other sequences
    val sawtoothTime =
        measureAverageExecutionTime(iterations) {
            val arr = Array(size) { 0 }
            sawtoothSequence(arr, interval, min)
        }
    println("Sawtooth sequence: $sawtoothTime мс")

    val sinusoidalTime =
        measureAverageExecutionTime(iterations) {
            val arr = Array(size) { 0 }
            sinusoidalSequence(arr, max, min)
        }
    println("Sinusoidal sequence: $sinusoidalTime мс")

    val staggeredTime =
        measureAverageExecutionTime(iterations) {
            val arr = Array(size) { 0 }
            staggeredSequence(arr, interval, delta, start)
        }
    println("Staggered sequence: $staggeredTime мс")

    val quasiOrderedTime =
        measureAverageExecutionTime(iterations) {
            val arr = Array(size) { 0 }
            quasiOrderedSequence(arr, start, delta)
        }
    println("Quasi-ordered sequence: $quasiOrderedTime мс")
}

fun testWithDoubleArrays(
    size: Int,
    iterations: Int,
) {
    val min = 0.0
    val max = 100.0
    val interval = 10
    val delta = 10.0
    val start = 0.0

    // Benchmark for Double arrays
    val increasingTime =
        measureAverageExecutionTime(iterations) {
            val arr = Array<Double>(size) { 0.0 }
            increasingSequence(arr)
        }
    println("Increasing sequence: $increasingTime мс")

    val decreasingTime =
        measureAverageExecutionTime(iterations) {
            val arr = Array<Double>(size) { 0.0 }
            decreasingSequence(arr)
        }
    println("Decreasing sequence: $decreasingTime мс")

    // Add the rest of the benchmarks for Double arrays
    val randomTime =
        measureAverageExecutionTime(iterations) {
            val arr = Array<Double>(size) { 0.0 }
            randomSequence(arr, max, min)
        }
    println("Random sequence: $randomTime мс")

    val sawtoothTime =
        measureAverageExecutionTime(iterations) {
            val arr = Array<Double>(size) { 0.0 }
            sawtoothSequence(arr, interval, min)
        }
    println("Sawtooth sequence: $sawtoothTime мс")

    val sinusoidalTime =
        measureAverageExecutionTime(iterations) {
            val arr = Array<Double>(size) { 0.0 }
            sinusoidalSequence(arr, max, min)
        }
    println("Sinusoidal sequence: $sinusoidalTime мс")

    val staggeredTime =
        measureAverageExecutionTime(iterations) {
            val arr = Array<Double>(size) { 0.0 }
            staggeredSequence(arr, interval, delta, start)
        }
    println("Staggered sequence: $staggeredTime мс")

    val quasiOrderedTime =
        measureAverageExecutionTime(iterations) {
            val arr = Array<Double>(size) { 0.0 }
            quasiOrderedSequence(arr, start, delta)
        }
    println("Quasi-ordered sequence: $quasiOrderedTime мс")
}
