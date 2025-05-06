package labs.lab2.var7

import labs.lab1.inverselyOrderedSequence
import labs.lab1.orderedSequence
import labs.lab1.randomSequence
import labs.lab1.stepwiseSequence

/**
 * Сортировка вставками с сигнальным ключом на IntArray.
 */
fun sentinelInsertionSortCount(arr: IntArray): Long {
    val n = arr.size
    var count = 0L

    // 1) Находим индекс минимального элемента
    var minIndex = 0
    for (i in 1 until n) {
        count++
        if (arr[i] < arr[minIndex]) {
            minIndex = i
        }
    }
    // 2) Меняем его с первым элементом — устанавливаем sentinel
    val tmp = arr[0]
    arr[0] = arr[minIndex]
    arr[minIndex] = tmp

    // 3) Обычная сортировка вставками без проверки границы
    for (i in 1 until n) {
        val key = arr[i]
        var j = i - 1
        while (true) {
            count++
            if (arr[j] > key) {
                arr[j + 1] = arr[j]
                j--
            } else {
                break
            }
        }
        arr[j + 1] = key
    }
    return count
}

/**
 * Бинарная сортировка вставками на IntArray.
 */
fun binaryInsertionSortCount(arr: IntArray): Long {
    val n = arr.size
    var count = 0L

    // Вспомогательная функция: ищет точку вставки в [start, end)
    // и увеличивает comparisons при каждом сравнении.
    fun findInsertionPoint(
        start: Int,
        end: Int,
        key: Int,
    ): Int {
        var low = start
        var high = end
        while (low < high) {
            val mid = (low + high) ushr 1
            count++ // сравнение arr[mid] и key
            if (arr[mid] < key) {
                low = mid + 1
            } else {
                high = mid
            }
        }
        return low
    }

    for (i in 1 until n) {
        val key = arr[i]
        // 1) находим insertionPoint с помощью собственного бинарного поиска
        val insertionPoint = findInsertionPoint(0, i, key)

        // 2) сдвигаем блок [insertionPoint, i) вправо на 1 позицию
        arr.copyInto(arr, insertionPoint + 1, insertionPoint, i)

        // 3) вставляем ключ
        arr[insertionPoint] = key
    }
    return count
}

// Главная функция, возвращающая количество операций сравнения
fun msdRadixSortCount(
    arr: IntArray,
    bitSize: Int = 8,
): Long {
    // Константа для XOR-преобразования для корректной обработки знаковых Int
    val signFlip = Int.MIN_VALUE

    // 1. Преобразование для правильной сортировки знаковых чисел
    for (i in arr.indices) {
        arr[i] = arr[i] xor signFlip
    }

    // Вспомогательный буфер для распределения
    val buffer = IntArray(arr.size)
    // Начинаем с самого старшего бита (31 для Int)
    val startBitPosition = 31

    // Запускаем рекурсию и получаем количество сравнений
    val comparisonCount = msdRadixSortRecursive(arr, 0, arr.lastIndex, startBitPosition, bitSize, buffer)

    // 3. Обратное преобразование
    for (i in arr.indices) {
        arr[i] = arr[i] xor signFlip
    }

    return comparisonCount
}

// Рекурсивная функция MSD-сортировки, теперь возвращает Long – число сравнений
private fun msdRadixSortRecursive(
    arr: IntArray,
    low: Int,
    high: Int,
    bitPosition: Int,
    bitSize: Int,
    buffer: IntArray,
): Long {
    var count = 0L

    // Проверяем базовые случаи: empty subarray или вышли за предел битов
    // Считаем два сравнения: high <= low и bitPosition < 0
    count += 1 // для high <= low
    val isEmpty = high <= low
    count += 1 // для bitPosition < 0
    val noBitsLeft = bitPosition < 0
    if (isEmpty || noBitsLeft) {
        return count
    }

    val bucketCount = 1 shl bitSize
    val mask = bucketCount - 1
    val shift = bitPosition - bitSize + 1

    val countArr = IntArray(bucketCount)
    val offset = IntArray(bucketCount)

    // Шаг 1: подсчёт элементов по корзинам
    for (i in low..high) {
        val bucketIndex = (arr[i] ushr shift) and mask
        countArr[bucketIndex]++
    }

    // Шаг 2: вычисление смещений
    offset[0] = low
    for (k in 1 until bucketCount) {
        offset[k] = offset[k - 1] + countArr[k - 1]
    }
    val originalOffsets = offset.copyOf()

    // Шаг 3: распределение во временный буфер
    for (i in low..high) {
        val bucketIndex = (arr[i] ushr shift) and mask
        buffer[offset[bucketIndex]] = arr[i]
        offset[bucketIndex]++
    }

    // Шаг 4: копирование обратно
    System.arraycopy(buffer, low, arr, low, high - low + 1)

    // Шаг 5: рекурсивные вызовы для каждой корзины
    val nextBitPosition = bitPosition - bitSize
    for (k in 0 until bucketCount) {
        // считаем одно сравнение bucketSize > 1
        count += 1
        val bucketStart = originalOffsets[k]
        val bucketEnd = if (k + 1 < bucketCount) originalOffsets[k + 1] else high + 1
        val bucketSize = bucketEnd - bucketStart

        if (bucketSize > 1) {
            // добавляем все сравнения из рекурсии
            count += msdRadixSortRecursive(arr, bucketStart, bucketEnd - 1, nextBitPosition, bitSize, buffer)
        }
    }

    return count
}

fun main() {
    // Определяем размеры массивов для тестирования
    val arraySizes = arrayOf(5000, 10000, 20000, 30000, 40000, 50000)

    // Определяем map функций-генераторов для разных типов массивов
    val arrayGenerators =
        mapOf(
            "Упорядоченный массив" to { size: Int -> IntArray(size).also { orderedSequence(it) } },
            "Обратно упорядоченный массив" to { size: Int -> IntArray(size).also { inverselyOrderedSequence(it) } },
            "Случайный массив" to { size: Int -> IntArray(size).also { randomSequence(it) } },
            "Ступенчатый массив" to { size: Int ->
                IntArray(size).also {
                    stepwiseSequence(
                        it,
                        size / 50,
                        20,
                        0,
                        1000,
                    )
                }
            },
        )

    // Определяем map функций сортировки и их названий
    val sortMap =
        mapOf(
            "Сортировка вставками (сигнальный ключ)" to ::sentinelInsertionSortCount,
            "Бинарная сортировка вставками" to ::binaryInsertionSortCount,
            "MSD поразрядная сортировка (8 бит)" to { arr: IntArray -> msdRadixSortCount(arr, 8) },
            "MSD поразрядная сортировка (4 бита)" to { arr: IntArray -> msdRadixSortCount(arr, 4) },
            "MSD поразрядная сортировка (2 бита)" to { arr: IntArray -> msdRadixSortCount(arr, 2) },
            "MSD поразрядная сортировка (1 бит)" to { arr: IntArray -> msdRadixSortCount(arr, 1) },
        )

    for ((arrayName, generator) in arrayGenerators) {
        println("\n--- Тип массива: $arrayName ---")
        for ((sortName, sortFunc) in sortMap) {
            println("\n--- Сортировка: $sortName ---")
            for (size in arraySizes) {
                val arr = generator(size)
                val count = sortFunc(arr)
                println("размер массива ${size.toString().padEnd(5)}: $count операций сравнения")
            }
        }
    }
}
