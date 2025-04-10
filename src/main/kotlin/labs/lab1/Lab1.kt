package labs.lab1

import kotlin.random.Random
import kotlin.system.measureNanoTime

fun orderedSequence(array: IntArray) {
    for (i in array.indices) {
        array[i] = i
    }
}

fun orderedSequence(array: DoubleArray) {
    for (i in array.indices) {
        array[i] = i.toDouble()
    }
}

fun inverselyOrderedSequence(array: IntArray) {
    for (i in array.indices) {
        array[i] = array.size - i - 1
    }
}

fun inverselyOrderedSequence(array: DoubleArray) {
    for (i in array.indices) {
        array[i] = (array.size - i - 1).toDouble()
    }
}

fun randomSequence(
    array: IntArray,
    min: Int = 0,
    max: Int = 100,
) {
    for (i in array.indices) {
        array[i] = Random.nextInt(min, max)
    }
}

fun randomSequence(
    array: DoubleArray,
    min: Double = 0.0,
    max: Double = 100.0,
) {
    for (i in array.indices) {
        array[i] = Random.nextDouble(min, max)
    }
}

fun sawtoothSequence(
    array: IntArray,
    min: Int = 0,
    max: Int = 100,
) {
    var interval = max - min + 1
    for (i in array.indices) {
        val step = i % interval
        array[i] = min + step
    }
}

fun sawtoothSequence(
    array: DoubleArray,
    min: Double = 0.0,
    max: Double = 100.0,
) {
    var interval = max - min
    for (i in array.indices) {
        val step = i.toDouble() % interval
        array[i] = min + step
    }
}

fun sinusoidalSequence(
    array: IntArray,
    min: Int = 0,
    max: Int = 100,
) {
    val range = max - min
    val cycleLen = 2 * range

    for (i in array.indices) {
        val posInCycle = i % cycleLen

        if (posInCycle < range) {
            array[i] = max - posInCycle
        } else {
            array[i] = min + (posInCycle - range)
        }
    }
}

fun sinusoidalSequence(
    array: DoubleArray,
    min: Double = 0.0,
    max: Double = 100.0,
) {
    val range = max - min
    val cycleLen = 2.0 * range

    for (i in array.indices) {
        val posInCycle = i.toDouble() % cycleLen

        if (posInCycle < range) {
            array[i] = max - posInCycle
        } else {
            array[i] = min + (posInCycle - range)
        }
    }
}

fun stepwiseSequence(
    array: IntArray,
    interval: Int,
    step: Int,
    initialMin: Int,
    initialMax: Int,
) {
    var currentMin = initialMin
    var currentMax = initialMax
    val size = array.size
    var i = 0

    while (i < size) {
        val chunkEndIndex = minOf(i + interval, size)

        while (i < chunkEndIndex) {
            array[i] = Random.nextInt(currentMin, currentMax)
            i++
        }

        currentMin += step
        currentMax += step
    }
}

fun stepwiseSequence(
    array: DoubleArray,
    interval: Int,
    step: Double,
    initialMin: Double,
    initialMax: Double,
) {
    var currentMin = initialMin
    var currentMax = initialMax
    val size = array.size
    var i = 0

    while (i < size) {
        val chunkEndIndex = minOf(i + interval, size)

        while (i < chunkEndIndex) {
            array[i] = Random.nextDouble(currentMin, currentMax)
            i++
        }

        currentMin += step
        currentMax += step
    }
}

fun quasiOrderedSequence(
    array: IntArray,
    interval: Int,
    initialMin: Int,
    initialMax: Int,
) {
    var currentMin = initialMin
    var currentMax = initialMax
    for (i in array.indices) {
        array[i] = Random.nextInt(currentMin, currentMax)
        currentMin += interval
        currentMax += interval
    }
}

fun quasiOrderedSequence(
    array: DoubleArray,
    interval: Double,
    initialMin: Double,
    initialMax: Double,
) {
    var currentMin = initialMin
    var currentMax = initialMax
    for (i in array.indices) {
        array[i] = Random.nextDouble(currentMin, currentMax)
        currentMin += interval
        currentMax += interval
    }
}

inline fun <T> measureAverageTimeWithWarmup(
    arraySize: Int,
    warmupIterations: Int,
    measurementIterations: Int,
    prepareArray: (Int) -> T, // T будет либо IntArray, либо DoubleArray
    operation: (T) -> Unit,
): Long {
    // --- Фаза прогрева ---
    repeat(warmupIterations) {
        val array = prepareArray(arraySize)
        operation(array)
    }

    // --- Фаза замера ---
    var totalTime = 0L
    repeat(measurementIterations) {
        val array = prepareArray(arraySize) // Создаем массив ПЕРЕД замером
        val time =
            measureNanoTime {
                operation(array) // Выполняем операцию
            }
        totalTime += time
    }
    return totalTime / measurementIterations
}

// --- Основная часть для запуска тестов ---

fun main() {
    val arraySize = 500_000 // Размер массива
    val warmupIterations = 50 // Количество итераций для прогрева
    val measurementIterations = 100 // Количество итераций для замера (увеличил для стабильности)

    println("Measuring average execution time for array size $arraySize")
    println("Warmup iterations: $warmupIterations")
    println("Measurement iterations: $measurementIterations\n")

    // ... (определение параметров minInt, maxInt и т.д. остается таким же) ...
    val minInt = 0
    val maxInt = 1000
    val minDouble = 0.0
    val maxDouble = 1000.0
    val stepIntInterval = arraySize / 50
    val stepIntStep = 20
    val stepDoubleInterval = arraySize / 50
    val stepDoubleStep = 20.0
    val quasiIntInterval = 1
    val quasiIntMin = 0
    val quasiIntMax = 10
    val quasiDoubleInterval = 0.01
    val quasiDoubleMin = 0.0
    val quasiDoubleMax = 1.0

    val results = mutableMapOf<String, Long>()

    // --- Замеры для IntArray ---
    results["orderedSequence (Int)"] =
        measureAverageTimeWithWarmup(arraySize, warmupIterations, measurementIterations, ::IntArray) {
            orderedSequence(
                it,
            )
        }
    results["inverselyOrderedSequence (Int)"] =
        measureAverageTimeWithWarmup(
            arraySize,
            warmupIterations,
            measurementIterations,
            ::IntArray,
        ) { inverselyOrderedSequence(it) }
    results["randomSequence (Int)"] =
        measureAverageTimeWithWarmup(
            arraySize,
            warmupIterations,
            measurementIterations,
            ::IntArray,
        ) { randomSequence(it, minInt, maxInt) }
    results["sawtoothSequence (Int)"] =
        measureAverageTimeWithWarmup(
            arraySize,
            warmupIterations,
            measurementIterations,
            ::IntArray,
        ) { sawtoothSequence(it, minInt, maxInt) }
    results["sinusoidalSequence (Int)"] =
        measureAverageTimeWithWarmup(
            arraySize,
            warmupIterations,
            measurementIterations,
            ::IntArray,
        ) { sinusoidalSequence(it, minInt, maxInt) }
    results["stepwiseSequence (Int)"] =
        measureAverageTimeWithWarmup(arraySize, warmupIterations, measurementIterations, ::IntArray) {
            stepwiseSequence(
                it,
                stepIntInterval,
                stepIntStep,
                minInt,
                maxInt / 10,
            )
        }
    results["quasiOrderedSequence (Int)"] =
        measureAverageTimeWithWarmup(arraySize, warmupIterations, measurementIterations, ::IntArray) {
            quasiOrderedSequence(it, quasiIntInterval, quasiIntMin, quasiIntMax)
        }

    // --- Замеры для DoubleArray ---
    results["orderedSequence (Double)"] =
        measureAverageTimeWithWarmup(
            arraySize,
            warmupIterations,
            measurementIterations,
            ::DoubleArray,
        ) { orderedSequence(it) }
    results["inverselyOrderedSequence (Double)"] =
        measureAverageTimeWithWarmup(
            arraySize,
            warmupIterations,
            measurementIterations,
            ::DoubleArray,
        ) { inverselyOrderedSequence(it) }
    results["randomSequence (Double)"] =
        measureAverageTimeWithWarmup(
            arraySize,
            warmupIterations,
            measurementIterations,
            ::DoubleArray,
        ) { randomSequence(it, minDouble, maxDouble) }
    results["sawtoothSequence (Double)"] =
        measureAverageTimeWithWarmup(arraySize, warmupIterations, measurementIterations, ::DoubleArray) {
            sawtoothSequence(it, minDouble, maxDouble)
        }
    results["sinusoidalSequence (Double)"] =
        measureAverageTimeWithWarmup(arraySize, warmupIterations, measurementIterations, ::DoubleArray) {
            sinusoidalSequence(it, minDouble, maxDouble)
        }
    results["stepwiseSequence (Double)"] =
        measureAverageTimeWithWarmup(arraySize, warmupIterations, measurementIterations, ::DoubleArray) {
            stepwiseSequence(
                it,
                stepDoubleInterval,
                stepDoubleStep,
                minDouble,
                maxDouble / 10.0,
            )
        }
    results["quasiOrderedSequence (Double)"] =
        measureAverageTimeWithWarmup(arraySize, warmupIterations, measurementIterations, ::DoubleArray) {
            quasiOrderedSequence(it, quasiDoubleInterval, quasiDoubleMin, quasiDoubleMax)
        }

    // --- Вывод результатов ---
    println("\n--- Results (Average time in milliseconds) ---")
    // Внутри цикла вывода результатов:
    results.forEach { (name, timeNano) ->
        val timeMillisDouble = timeNano / 1_000_000.0 // Деление на double дает double
        println("${name.padEnd(35)}: ${"%.3f".format(timeMillisDouble)} ms") // Форматируем double с 3 знаками
    }
}
