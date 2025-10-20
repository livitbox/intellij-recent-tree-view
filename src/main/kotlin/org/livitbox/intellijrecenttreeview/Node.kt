package org.livitbox.intellijrecenttreeview

class Node(
    private val key: String,
    private val fullPath: String,
    private val children: MutableMap<String, Node> = mutableMapOf(),
    private val parent: Node?
) {

    constructor(key: String, fullPath: String, parent: Node?) : this(key, fullPath, mutableMapOf(), parent)

    fun addChild(child: Node) {
        children[child.key] = child
    }

    fun getKey(): String {
        return key
    }

    fun getParent(): Node? {
        return parent
    }

    fun getChild(key: String): Node? {
        return children[key]
    }

    fun childSize(): Int {
        return children.size
    }

    fun traverse(depth: Int, action: (Node, Int) -> Unit) {
        action(this, depth)
        for (child in children.values) {
            child.traverse(depth + 1, action)
        }
    }

    fun getFullPath(): String {
        return fullPath
    }

    override fun toString(): String {
        return "Node(key='$key', fullPath='$fullPath', childrenSize=${children.size}, hasParent=${parent != null})"
    }
}
