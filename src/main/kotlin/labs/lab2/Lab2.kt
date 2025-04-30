package labs.lab2

/**
 * Сортировка вставками с сигнальным ключом на IntArray.
 */
fun sentinelInsertionSort(arr: IntArray) {
    val n = arr.size
    if (n < 2) return

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
        for (j in i downTo insertionPoint + 1) {
            arr[j] = arr[j - 1]
        }
        // 3) вставляем ключ
        arr[insertionPoint] = key
    }
}

fun msdRadixSort(
    arr: IntArray,
    bitsPerDigit: Int = 4,
) {
    val maxDigits = (32 + bitsPerDigit - 1) / bitsPerDigit
    val bucketCount = 1 shl bitsPerDigit
    val mask = bucketCount - 1

    fun sort(
        from: Int,
        to: Int,
        digit: Int,
    ) {
        if (digit == maxDigits || to - from <= 1) return

        // 1) Собираем корзины
        val buckets = Array(bucketCount) { mutableListOf<Int>() }
        val shift = (maxDigits - 1 - digit) * bitsPerDigit
        for (i in from until to) {
            val key = (arr[i].ushr(shift)) and mask
            buckets[key].add(arr[i])
        }

        // 2) Записываем обратно и рекурсивно сортируем каждую корзину
        var index = from
        for (bucket in buckets) {
            for (v in bucket) {
                arr[index++] = v
            }
            // если в корзине >1, продолжаем по следующему разряду
            if (bucket.size > 1) {
                sort(index - bucket.size, index, digit + 1)
            }
        }
    }

    sort(0, arr.size, digit = 0)
}

// --- Тестируем ---
fun main() {
    val data = intArrayOf(170, 45, 75, 90, 802, 24, 2, 66)
    println("До сортировки: ${data.joinToString()}")
    msdRadixSort(data, bitsPerDigit = 4)
    println("После MSD-сортировки: ${data.joinToString()}")
}
