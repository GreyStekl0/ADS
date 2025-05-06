package labs.lab2.var7

import labs.lab1.inverselyOrderedSequence
import labs.lab1.orderedSequence
import labs.lab1.randomSequence
import labs.lab1.stepwiseSequence
import kotlin.system.measureNanoTime

/**
 * Сортировка вставками с сигнальным ключом на IntArray.
 */
fun sentinelInsertionSort(arr: IntArray) {
    val n = arr.size

    // 1) Находим индекс минимального элемента
    var minIndex = 0
    for (i in 1 until n) {
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
        while (arr[j] > key) {
            arr[j + 1] = arr[j]
            j--
        }
        arr[j + 1] = key
    }
}

/**
 * Бинарная сортировка вставками на IntArray.
 */
fun binaryInsertionSort(arr: IntArray) {
    val n = arr.size
    for (i in 1 until n) {
        val key = arr[i]
        // 1) находим insertionPoint с помощью binarySearch
        val idx = arr.binarySearch(key, 0, i)
        val insertionPoint = if (idx >= 0) idx else -idx - 1

        // 2) сдвигаем элементы вправо
//        for (j in i downTo insertionPoint + 1) {
//            arr[j] = arr[j - 1]
//        }
        arr.copyInto(arr, insertionPoint + 1, insertionPoint, i)

        // 3) вставляем ключ
        arr[insertionPoint] = key
    }
}

// Главная функция, вызывающая рекурсивную сортировку
fun msdRadixSort(
    arr: IntArray,
    bitSize: Int = 8,
) {
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

    msdRadixSortRecursive(arr, 0, arr.size - 1, startBitPosition, bitSize, buffer)

    // 3. Обратное преобразование
    for (i in arr.indices) {
        arr[i] = arr[i] xor signFlip
    }
}

// Рекурсивная функция MSD сортировки
private fun msdRadixSortRecursive(
    arr: IntArray,
    low: Int,
    high: Int,
    bitPosition: Int, // Индекс *самого левого* бита текущей группы
    bitSize: Int,
    buffer: IntArray,
) {
    // Базовые случаи рекурсии:
    // 1. Подмассив пуст или содержит 1 элемент
    // 2. Все биты обработаны (bitPosition < 0)
    if (high <= low || bitPosition < 0) {
        return
    }

    val bucketCount = 1 shl bitSize // Количество корзин = 2^bitSize
    val mask = bucketCount - 1 // Маска для выделения нужных битов
    // Сдвиг, чтобы нужная группа битов оказалась справа
    val shift = bitPosition - bitSize + 1

    val count = IntArray(bucketCount) // Массив для подсчета элементов в каждой корзине
    val offset = IntArray(bucketCount) // Массив для хранения начальных позиций корзин

    // --- Шаг 1: Подсчет (Counting) ---
    for (i in low..high) {
        // Извлекаем нужную группу битов
        // Используем беззнаковый сдвиг (ushr) т.к. работаем с XOR-нутыми числами как с беззнаковыми
        val bucketIndex = (arr[i] ushr shift) and mask
        count[bucketIndex]++
    }

    // --- Шаг 2: Вычисление смещений (Offsets) ---
    // offset[k] будет хранить позицию, куда помещать *первый* элемент из корзины k
    offset[0] = low // Первая корзина начинается с индекса low текущего подмассива
    for (k in 1 until bucketCount) {
        offset[k] = offset[k - 1] + count[k - 1]
    }

    // Копируем смещения перед их изменением на шаге распределения,
    // чтобы знать границы для рекурсивных вызовов
    val originalOffsets = offset.copyOf()

    // --- Шаг 3: Распределение (Distribution) ---
    for (i in low..high) {
        val bucketIndex = (arr[i] ushr shift) and mask
        // Помещаем элемент во временный буфер на правильную позицию
        buffer[offset[bucketIndex]] = arr[i]
        // Сдвигаем позицию для следующего элемента этой же корзины
        offset[bucketIndex]++
    }

    // --- Шаг 4: Копирование обратно в основной массив ---
    System.arraycopy(buffer, low, arr, low, high - low + 1)
    // или вручную: for (i in low..high) { arr[i] = buffer[i] }

    // --- Шаг 5: Рекурсивные вызовы для каждой корзины ---
    // Следующая группа битов будет левее на bitSize
    val nextBitPosition = bitPosition - bitSize
    for (k in 0 until bucketCount) {
        // Определяем границы подмассива для текущей корзины k
        val bucketStart = originalOffsets[k]
        // Конец корзины k - это начало корзины k+1 (или high+1 для последней корзины)
        val bucketEnd = if (k + 1 < bucketCount) originalOffsets[k + 1] else high + 1
        val bucketSize = bucketEnd - bucketStart

        // Рекурсивно сортируем только непустые корзины (> 1 элемента)
        if (bucketSize > 1) {
            msdRadixSortRecursive(arr, bucketStart, bucketEnd - 1, nextBitPosition, bitSize, buffer)
        }
    }
}

// Функция для измерения времени сортировки с разогревом и усреднением
fun measureSortTimeSeconds(
    prepareArray: IntArray,
    sort: (IntArray) -> Unit,
    warmupRuns: Int = 1,
    measureRuns: Int = 3,
) {
    // Прогрев
    repeat(warmupRuns) {
        val arr = prepareArray.copyOf()
        sort(arr)
    }
    // Замеры
    var totalTime = 0L
    repeat(measureRuns) {
        val arr = prepareArray.copyOf()
        val t = measureNanoTime { sort(arr) }
        totalTime += t
    }
    // Расчет среднего времени в миллисекундах
    val totalMillis = totalTime / measureRuns / 1_000_000.0

    // Вывод результата с округлением до 3 знаков
    println("%.3f мс".format(totalMillis))
}

// Пример использования:
fun main() {
    val arraySizes = arrayOf(5000, 10000, 20000, 30000, 40000, 50000)
    val warmupIterations = 2
    val measurementIterations = 3

    // Определяем map функций-генераторов для разных типов массивов
    val arrayGenerators =
        mapOf(
            "Упорядоченный массив" to { size: Int -> IntArray(size).also { orderedSequence(it) } },
            "Обратно упорядоченный массив" to { size: Int -> IntArray(size).also { inverselyOrderedSequence(it) } },
            "Случайный массив" to { size: Int -> IntArray(size).also { randomSequence(it) } },
            "Ступенчатый массив" to { size: Int ->
                IntArray(size).also {
                    stepwiseSequence(it, size / 50, 20, 0, 1000)
                }
            },
        )

    // Определяем map функций сортировки и их названий
    val sortMap =
        mapOf(
            "Сортировка вставками (сигнальный ключ)" to ::sentinelInsertionSort,
            "Бинарная сортировка вставками" to ::binaryInsertionSort,
            "MSD поразрядная сортировка (8 бит)" to { arr: IntArray -> msdRadixSort(arr, 8) },
            "MSD поразрядная сортировка (4 бита)" to { arr: IntArray -> msdRadixSort(arr, 4) },
            "MSD поразрядная сортировка (2 бита)" to { arr: IntArray -> msdRadixSort(arr, 2) },
            "MSD поразрядная сортировка (1 бит)" to { arr: IntArray -> msdRadixSort(arr, 1) },
        )

    println("\n--- Измерение времени сортировки (прогрев: $warmupIterations, замеры: $measurementIterations) ---")

    for ((arrayName, generator) in arrayGenerators) {
        println("\n--- Тип массива: $arrayName ---")
        for ((sortName, sortFunc) in sortMap) {
            println("\n--- Сортировка: $sortName ---")
            for (size in arraySizes) {
                val arr = generator(size)
                print("размер массива ${size.toString().padEnd(5)}: ")
                measureSortTimeSeconds(
                    prepareArray = arr,
                    sort = sortFunc,
                    warmupRuns = warmupIterations,
                    measureRuns = measurementIterations,
                )
            }
        }
    }
}
