package org.livitbox.intellijrecenttreeview.model

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.DefaultActionGroup

class TreeNodeBranch : DefaultActionGroup {

    val key: String
    val fullPath: String
    private val children: HashMap<String, AnAction>
    val parent: TreeNodeBranch?

    constructor(
        key: String,
        fullPath: String,
        children: HashMap<String, AnAction>,
        parent: TreeNodeBranch?
    ) : super(key, true) {
        this.key = key
        this.fullPath = fullPath
        this.children = children
        this.parent = parent
    }

    constructor(
        key: String,
        fullPath: String,
        parent: TreeNodeBranch?
    ) : this(key, fullPath, hashMapOf(), parent)

    fun addChild(key: String, child: AnAction) {
        children[key] = child
        super.addAction(child)
    }

    fun getChild(key: String): AnAction? {
        return children[key]
    }

    fun sortChildren(comparator: Comparator<String>) {
        if (children.size > 1) {
            val sortedChildren = children.toSortedMap(comparator)
            super.removeAll()
            super.addAll(sortedChildren.values)
        }
        children.values.filterIsInstance<TreeNodeBranch>().forEach { it.sortChildren(comparator) }
    }

    override fun toString(): String {
        return "TreeNodeBranch(key='$key', fullPath='$fullPath', childrenSize=${children.size}, hasParent=${parent != null})"
    }
}
