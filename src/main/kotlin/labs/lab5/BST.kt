package labs.lab5

// Определение узла дерева
data class Node(
    var data: Int,
    var left: Node? = null,
    var right: Node? = null,
)

// Класс бинарного дерева поиска
class BinarySearchTree {
    var root: Node? = null

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
        } else {
            current.right = insertRecursive(current.right, data)
        }
        return current
    }
}
