package labs.lab1

import kotlin.random.Random

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
        val posInCycle = i % cycleLen

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

fun main() {
    val intArray = IntArray(40)
    val doubleArray = DoubleArray(40)
    quasiOrderedSequence(intArray, interval = 5, 10, 30)
    println(intArray.joinToString(", "))
    stepwiseSequence(doubleArray, interval = 10, step = 10.0, 0.0, 10.0)
    println(doubleArray.joinToString(", "))
}
