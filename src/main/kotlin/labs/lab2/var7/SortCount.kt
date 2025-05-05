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

class MsdRadixSorter(
    private val bitSize: Int = 8,
) {
    // Сюда накапливаем все сравнения
    private var comparisonCount = 0L

    fun sort(arr: IntArray): Long {
        // 1) Преобразование для signed→unsigned
        val signFlip = Int.MIN_VALUE
        for (i in arr.indices) {
            arr[i] = arr[i] xor signFlip
        }

        // 2) Самая младшая «точка входа»
        val buffer = IntArray(arr.size)
        val startBit = 31
        msdRadixSortRecursive(arr, 0, arr.lastIndex, startBit, buffer)

        // 3) Обратное преобразование
        for (i in arr.indices) {
            arr[i] = arr[i] xor signFlip
        }

        println("Total comparisons: $comparisonCount")
        return comparisonCount
    }

    private fun msdRadixSortRecursive(
        arr: IntArray,
        low: Int,
        high: Int,
        bitPosition: Int,
        buffer: IntArray,
    ) {
        // 1. Проверяем базовые случаи
        comparisonCount++ // для high <= low
        if (high <= low) return

        comparisonCount++ // для bitPosition < 0
        if (bitPosition < 0) return

        val bucketCount = 1 shl bitSize
        val mask = bucketCount - 1
        val shift = bitPosition - bitSize + 1

        // Подсчёт и смещения как у вас
        val count = IntArray(bucketCount)
        val offset = IntArray(bucketCount)
        for (i in low..high) {
            val idx = (arr[i] ushr shift) and mask
            count[idx]++
        }
        offset[0] = low
        for (k in 1 until bucketCount) {
            offset[k] = offset[k - 1] + count[k - 1]
        }
        val origOffset = offset.copyOf()

        // Распределение
        for (i in low..high) {
            val idx = (arr[i] ushr shift) and mask
            buffer[offset[idx]] = arr[i]
            offset[idx]++
        }
        System.arraycopy(buffer, low, arr, low, high - low + 1)

        // Рекурсивные вызовы по корзинам
        val nextBit = bitPosition - bitSize
        for (k in 0 until bucketCount) {
            val start = origOffset[k]
            // конец корзины
            val end = if (k + 1 < bucketCount) origOffset[k + 1] else high + 1
            val size = end - start

            comparisonCount++ // для bucketSize > 1
            if (size > 1) {
                msdRadixSortRecursive(arr, start, end - 1, nextBit, buffer)
            }
        }
    }
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
    val arraySize = 100_000

    // Генерируем базовые массивы
    val orderedArr = IntArray(arraySize).also { orderedSequence(it) }
    val inverseArr = IntArray(arraySize).also { inverselyOrderedSequence(it) }
    val randomArr = IntArray(arraySize).also { randomSequence(it) }
    val stepwiseArr = IntArray(arraySize).also { stepwiseSequence(it, arraySize / 50, 20, 0, 1000) }

    // Определяем названия для массивов и сортировок для более читаемого вывода
    val arrayMap =
        mapOf(
            "Упорядоченный массив" to orderedArr,
            "Обратно упорядоченный массив" to inverseArr,
            "Случайный массив" to randomArr,
            "Ступенчатый массив" to stepwiseArr,
        )

    val sortMap =
        mapOf(
            "Сортировка вставками (сигнальный ключ)" to ::sentinelInsertionSortCount,
            "Бинарная сортировка вставками" to ::binaryInsertionSortCount,
            "MSD поразрядная сортировка (8 бит)" to { arr: IntArray -> msdRadixSortCount(arr, 8) },
            "MSD поразрядная сортировка (4 бита)" to { arr: IntArray -> msdRadixSortCount(arr, 4) },
            "MSD поразрядная сортировка (2 бита)" to { arr: IntArray -> msdRadixSortCount(arr, 2) },
            "MSD поразрядная сортировка (1 бит)" to { arr: IntArray -> msdRadixSortCount(arr, 1) },
        )

    // Сортируем и выводим результаты
    for ((arrayName, array) in arrayMap) {
        println("\n--- Тип массива: $arrayName ---")
        for ((sortName, sortFunc) in sortMap) {
            val arrCopy = array.copyOf()
            val count = sortFunc(arrCopy)
            println("${sortName.padEnd(40)}: $count операций сравнения")
        }
    }
}
