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
        array[i] = min + (i % interval)
    }
}

fun sawtoothSequence(
    array: DoubleArray,
    min: Double = 0.0,
    max: Double = 100.0,
) {
    var interval = (max - min).toInt() + 1
    for (i in array.indices) {
        val step = i % interval
        array[i] = min + step.toDouble()
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

fun stepwiseSequence() {
}

fun main() {
    val intArray = IntArray(11)
    val doubleArray = DoubleArray(20)
    sinusoidalSequence(intArray, 0, 5)
    println(intArray.joinToString(", "))
    sinusoidalSequence(doubleArray, 0.6, 5.6)
    println(doubleArray.joinToString(", "))
}
