package labs.lab5

import kotlin.random.Random

// Определение узла дерева
data class Node(
    var data: Int,
    var left: Node? = null,
    var right: Node? = null,
)

// Класс бинарного дерева поиска
class BinarySearchTree {
    var root: Node? = null

    // 1. Вставка узла
    fun insert(data: Int) {
        root = insertRecursive(root, data)
    }

    private fun insertRecursive(
        current: Node?,
        data: Int,
    ): Node {
        if (current == null) {
            return Node(data)
        }

        if (data < current.data) {
            current.left = insertRecursive(current.left, data)
        } else if (data > current.data) {
            current.right = insertRecursive(current.right, data)
        }
        return current
    }

    // 2. Поиск узла
    fun search(data: Int): Boolean = searchRecursive(root, data)

    private fun searchRecursive(
        current: Node?,
        data: Int,
    ): Boolean {
        if (current == null) {
            return false
        }

        return when {
            data == current.data -> true
            data < current.data -> searchRecursive(current.left, data)
            else -> searchRecursive(current.right, data)
        }
    }

    // 3. Удаление узла
    fun delete(data: Int) {
        root = deleteRecursive(root, data)
    }

    private fun deleteRecursive(
        current: Node?,
        data: Int,
    ): Node? {
        if (current == null) {
            return null
        }

        when {
            data < current.data -> current.left = deleteRecursive(current.left, data)
            data > current.data -> current.right = deleteRecursive(current.right, data)
            else -> {
                // Узел найден, нужно удалить
                return when {
                    current.left == null && current.right == null -> null
                    current.left == null -> current.right
                    current.right == null -> current.left
                    else -> {
                        // У узла есть оба потомка
                        val minRight = findMin(current.right!!)
                        current.data = minRight.data
                        current.right = deleteRecursive(current.right, minRight.data)
                        current
                    }
                }
            }
        }
        return current
    }

    private fun findMin(node: Node): Node {
        var current = node
        while (current.left != null) {
            current = current.left!!
        }
        return current
    }

    // 4. Обходы дерева
    fun inorderTraversal(): List<Int> {
        val result = mutableListOf<Int>()
        inorderRecursive(root, result)
        return result
    }

    private fun inorderRecursive(
        node: Node?,
        result: MutableList<Int>,
    ) {
        if (node != null) {
            inorderRecursive(node.left, result)
            result.add(node.data)
            inorderRecursive(node.right, result)
        }
    }

    fun preorderTraversal(): List<Int> {
        val result = mutableListOf<Int>()
        preorderRecursive(root, result)
        return result
    }

    private fun preorderRecursive(
        node: Node?,
        result: MutableList<Int>,
    ) {
        if (node != null) {
            result.add(node.data)
            preorderRecursive(node.left, result)
            preorderRecursive(node.right, result)
        }
    }

    fun postorderTraversal(): List<Int> {
        val result = mutableListOf<Int>()
        postorderRecursive(root, result)
        return result
    }

    private fun postorderRecursive(
        node: Node?,
        result: MutableList<Int>,
    ) {
        if (node != null) {
            postorderRecursive(node.left, result)
            postorderRecursive(node.right, result)
            result.add(node.data)
        }
    }

    // 5. Вставка в корень
    fun insertAtRoot(data: Int) {
        root = insertAtRootRecursive(root, data)
    }

    private fun insertAtRootRecursive(
        node: Node?,
        data: Int,
    ): Node {
        if (node == null) {
            return Node(data)
        }

        if (data < node.data) {
            node.left = insertAtRootRecursive(node.left, data)
            return rotateRight(node)
        } else {
            node.right = insertAtRootRecursive(node.right, data)
            return rotateLeft(node)
        }
    }

    private fun rotateRight(node: Node): Node {
        val newRoot = node.left!!
        node.left = newRoot.right
        newRoot.right = node
        return newRoot
    }

    private fun rotateLeft(node: Node): Node {
        val newRoot = node.right!!
        node.right = newRoot.left
        newRoot.left = node
        return newRoot
    }

    // 6. Вывод дерева на экран
    fun printTree() {
        if (root == null) {
            println("Дерево пустое")
            return
        }
        printTreeRecursive(root, "", true)
    }

    private fun printTreeRecursive(
        node: Node?,
        prefix: String,
        isLast: Boolean,
    ) {
        if (node != null) {
            println(prefix + (if (isLast) "└── " else "├── ") + node.data)

            if (node.left != null || node.right != null) {
                if (node.left != null) {
                    printTreeRecursive(node.left, prefix + (if (isLast) "    " else "│   "), node.right == null)
                }
                if (node.right != null) {
                    printTreeRecursive(node.right, prefix + (if (isLast) "    " else "│   "), true)
                }
            }
        }
    }

    // 7. Высота дерева
    fun getHeight(): Int = getHeightRecursive(root)

    private fun getHeightRecursive(node: Node?): Int {
        if (node == null) {
            return 0
        }
        return 1 + maxOf(getHeightRecursive(node.left), getHeightRecursive(node.right))
    }

    // 8. Количество узлов
    fun getNodeCount(): Int = getNodeCountRecursive(root)

    private fun getNodeCountRecursive(node: Node?): Int {
        if (node == null) {
            return 0
        }
        return 1 + getNodeCountRecursive(node.left) + getNodeCountRecursive(node.right)
    }

    // 9. Рандомизированная вставка
    fun randomizedInsert(data: Int) {
        root = randomizedInsertRecursive(root, data, getNodeCountRecursive(root) + 1)
    }

    private fun randomizedInsertRecursive(
        node: Node?,
        data: Int,
        size: Int,
    ): Node {
        if (node == null) {
            return Node(data)
        }

        // С вероятностью 1/size вставляем в корень
        if (Random.nextInt(size) == 0) {
            return insertAtRootRecursive(node, data)
        }

        if (data < node.data) {
            node.left = randomizedInsertRecursive(node.left, data, size)
        } else {
            node.right = randomizedInsertRecursive(node.right, data, size)
        }
        return node
    }

    // 10. Подсчет суммы длин путей от корня до узлов с четными числами
    fun sumOfPathsToEvenNodes(): Int = sumOfPathsToEvenNodesRecursive(root, 0)

    private fun sumOfPathsToEvenNodesRecursive(
        node: Node?,
        depth: Int,
    ): Int {
        if (node == null) {
            return 0
        }

        var sum = 0

        // Если текущий узел содержит четное число, добавляем длину пути
        if (node.data % 2 == 0) {
            sum += depth
        }

        // Рекурсивно обходим левое и правое поддеревья
        sum += sumOfPathsToEvenNodesRecursive(node.left, depth + 1)
        sum += sumOfPathsToEvenNodesRecursive(node.right, depth + 1)

        return sum
    }

    // Дополнительные вспомогательные методы

    // Получить список всех четных узлов с их глубинами
    fun getEvenNodesWithDepths(): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        getEvenNodesWithDepthsRecursive(root, 0, result)
        return result
    }

    private fun getEvenNodesWithDepthsRecursive(
        node: Node?,
        depth: Int,
        result: MutableList<Pair<Int, Int>>,
    ) {
        if (node == null) return

        if (node.data % 2 == 0) {
            result.add(Pair(node.data, depth))
        }

        getEvenNodesWithDepthsRecursive(node.left, depth + 1, result)
        getEvenNodesWithDepthsRecursive(node.right, depth + 1, result)
    }

    // Проверка, является ли дерево пустым
    fun isEmpty(): Boolean = root == null

    // Очистка дерева
    fun clear() {
        root = null
    }
}

// Пример использования
fun main() {
    val bst = BinarySearchTree()

    // Обычная вставка
    println("=== Обычная вставка ===")
    val values = listOf(50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45)
    values.forEach { bst.insert(it) }

    println("Дерево:")
    bst.printTree()

    println("\nОбходы дерева:")
    println("Inorder: ${bst.inorderTraversal()}")
    println("Preorder: ${bst.preorderTraversal()}")
    println("Postorder: ${bst.postorderTraversal()}")

    println("\nИнформация о дереве:")
    println("Высота: ${bst.getHeight()}")
    println("Количество узлов: ${bst.getNodeCount()}")

    println("\nПоиск:")
    println("Поиск 40: ${bst.search(40)}")
    println("Поиск 100: ${bst.search(100)}")

    println("\n=== Анализ четных узлов ===")
    val evenNodes = bst.getEvenNodesWithDepths()
    println("Четные узлы и их глубины:")
    evenNodes.forEach { (value, depth) ->
        println("Значение: $value, Глубина: $depth")
    }
    println("Сумма длин путей до четных узлов: ${bst.sumOfPathsToEvenNodes()}")

    println("\n=== Удаление узла ===")
    println("Удаляем узел 30")
    bst.delete(30)
    bst.printTree()

    println("\n=== Вставка в корень ===")
    val bst2 = BinarySearchTree()
    listOf(50, 30, 70, 20, 40).forEach { bst2.insertAtRoot(it) }
    println("Дерево с вставкой в корень:")
    bst2.printTree()

    println("\n=== Рандомизированное дерево ===")
    val bst3 = BinarySearchTree()
    listOf(50, 30, 70, 20, 40, 60, 80).forEach { bst3.randomizedInsert(it) }
    println("Рандомизированное дерево:")
    bst3.printTree()
}
