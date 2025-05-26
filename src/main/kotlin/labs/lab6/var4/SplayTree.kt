package labs.lab6.var4

/**
 * Узел скошенного дерева
 */
data class SplayNode(
    var key: Int,
    var left: SplayNode? = null,
    var right: SplayNode? = null,
    var parent: SplayNode? = null,
)

/**
 * Класс скошенного дерева
 */
class SplayTree {
    var root: SplayNode? = null

    /**
     * Правый поворот
     * Поднимает левого потомка узла x на его место
     */
    private fun rightRotate(x: SplayNode): SplayNode {
        val y = x.left!!
        x.left = y.right
        y.right?.parent = x
        y.parent = x.parent

        when {
            x.parent == null -> root = y
            x == x.parent!!.right -> x.parent!!.right = y
            else -> x.parent!!.left = y
        }

        y.right = x
        x.parent = y
        return y
    }

    /**
     * Левый поворот
     * Поднимает правого потомка узла x на его место
     */
    private fun leftRotate(x: SplayNode): SplayNode {
        val y = x.right!!
        x.right = y.left
        y.left?.parent = x
        y.parent = x.parent

        when {
            x.parent == null -> root = y
            x == x.parent!!.left -> x.parent!!.left = y
            else -> x.parent!!.right = y
        }

        y.left = x
        x.parent = y
        return y
    }

    /**
     * Операция splay - поднимает узел x к корню дерева
     * Использует различные комбинации поворотов в зависимости от конфигурации
     */
    private fun splay(x: SplayNode) {
        while (x.parent != null) {
            val parent = x.parent!!
            val grandparent = parent.parent

            when {
                // Случай 1: родитель - корень (Zig)
                grandparent == null -> {
                    if (x == parent.left) {
                        rightRotate(parent)
                    } else {
                        leftRotate(parent)
                    }
                }

                // Случай 2: Zig-Zig (x и parent оба левые или оба правые потомки)
                (x == parent.left && parent == grandparent.left) -> {
                    rightRotate(grandparent)
                    rightRotate(parent)
                }

                (x == parent.right && parent == grandparent.right) -> {
                    leftRotate(grandparent)
                    leftRotate(parent)
                }

                // Случай 3: Zig-Zag (x и parent разнонаправленные потомки)
                (x == parent.right && parent == grandparent.left) -> {
                    leftRotate(parent)
                    rightRotate(grandparent)
                }

                else -> { // x == parent.left && parent == grandparent.right
                    rightRotate(parent)
                    leftRotate(grandparent)
                }
            }
        }
        root = x
    }

    /**
     * Поиск узла с заданным ключом
     * После поиска найденный (или последний посещенный) узел поднимается к корню
     */
    fun search(key: Int): SplayNode? {
        var current = root
        var lastVisited = root

        while (current != null) {
            lastVisited = current
            current =
                when {
                    key == current.key -> {
                        splay(current)
                        return current
                    }

                    key < current.key -> current.left
                    else -> current.right
                }
        }

        // Если ключ не найден, поднимаем последний посещенный узел
        lastVisited?.let { splay(it) }
        return null
    }

    /**
     * Вставка ключа в скошенное дерево
     * После вставки новый узел поднимается к корню
     */
    fun insert(key: Int) {
        // Если дерево пустое
        if (root == null) {
            root = SplayNode(key)
            return
        }

        // Поиск места для вставки
        var current = root!!

        while (true) {
            when {
                key == current.key -> {
                    // Ключ уже существует, просто поднимаем его к корню
                    splay(current)
                    return
                }

                key < current.key -> {
                    if (current.left == null) {
                        current.left = SplayNode(key, parent = current)
                        splay(current.left!!)
                        return
                    }
                    current = current.left!!
                }

                else -> {
                    if (current.right == null) {
                        current.right = SplayNode(key, parent = current)
                        splay(current.right!!)
                        return
                    }
                    current = current.right!!
                }
            }
        }
    }

    /**
     * Вспомогательная функция для вывода дерева на экран
     */
    fun printTree() {
        if (root == null) {
            println("Дерево пустое")
            return
        }
        println("Структура дерева:")
        printTreeHelper(root!!, "", true)
    }

    private fun printTreeHelper(
        node: SplayNode,
        prefix: String,
        isLast: Boolean,
    ) {
        println(prefix + (if (isLast) "└── " else "├── ") + node.key)

        val children = listOfNotNull(node.left, node.right)
        children.forEachIndexed { index, child ->
            val isLastChild = index == children.size - 1
            val newPrefix = prefix + (if (isLast) "    " else "│   ")
            printTreeHelper(child, newPrefix, isLastChild)
        }
    }

    /**
     * Симметричный обход дерева (для проверки корректности)
     */
    fun inorderTraversal(): List<Int> {
        val result = mutableListOf<Int>()
        inorderHelper(root, result)
        return result
    }

    private fun inorderHelper(
        node: SplayNode?,
        result: MutableList<Int>,
    ) {
        if (node != null) {
            inorderHelper(node.left, result)
            result.add(node.key)
            inorderHelper(node.right, result)
        }
    }

    /**
     * Получение высоты дерева
     */
    fun getHeight(): Int = getHeightHelper(root)

    private fun getHeightHelper(node: SplayNode?): Int {
        if (node == null) return 0
        return 1 + maxOf(getHeightHelper(node.left), getHeightHelper(node.right))
    }
}

/**
 * Тестовое приложение
 */
fun main() {
    val splayTree = SplayTree()

    println("=== Демонстрация работы скошенного дерева ===\n")

    // Вставка элементов
    val elements = listOf(10, 5, 15, 3, 7, 12, 18, 1, 4, 6, 8, 11, 13, 16, 20)

    println("Вставляем элементы: ${elements.joinToString(", ")}")
    elements.forEach { element ->
        println("\nВставляем $element:")
        splayTree.insert(element)
        println("Корень после вставки: ${splayTree.root?.key}")
        splayTree.printTree()
    }

    println("\n=== Проверка симметричного обхода ===")
    val inorder = splayTree.inorderTraversal()
    println("Симметричный обход: ${inorder.joinToString(", ")}")
    println("Высота дерева: ${splayTree.getHeight()}")

    println("\n=== Демонстрация операции поиска ===")
    val searchKeys = listOf(7, 13, 25)

    searchKeys.forEach { key ->
        println("\nПоиск ключа $key:")
        val found = splayTree.search(key)
        if (found != null) {
            println("Ключ $key найден, новый корень: ${splayTree.root?.key}")
        } else {
            println("Ключ $key не найден, корень после поиска: ${splayTree.root?.key}")
        }
        splayTree.printTree()
    }

    println("\n=== Финальное состояние дерева ===")
    println("Корень: ${splayTree.root?.key}")
    println("Высота: ${splayTree.getHeight()}")
    println("Элементы в порядке возрастания: ${splayTree.inorderTraversal().joinToString(", ")}")
}
