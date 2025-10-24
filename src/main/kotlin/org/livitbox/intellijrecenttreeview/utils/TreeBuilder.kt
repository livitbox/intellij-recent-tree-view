package org.livitbox.intellijrecenttreeview.utils

import com.intellij.openapi.actionSystem.AnAction
import org.livitbox.intellijrecenttreeview.model.TreeNodeBranch
import org.livitbox.intellijrecenttreeview.settings.RecentProjectsTreeViewSettingsState
import java.nio.file.Path

class TreeBuilder(val settings: RecentProjectsTreeViewSettingsState) {

    private val sortTreeBranchesComparator = Comparator<String> { a, b ->
        String.CASE_INSENSITIVE_ORDER.compare(a, b)
    }

    fun buildTree(paths: List<Path>): List<AnAction> {
        val tree = mutableListOf<AnAction>()
        if (paths.isEmpty()) {
            return tree
        }

        val rootNode = createRootNode(paths[0])
        for (pathStr in paths) {
            addPathAsNode(rootNode, pathStr)
        }

        if (settings.sortTreeBranches) {
            rootNode.sortChildren(sortTreeBranchesComparator)
        }

        val firstChildWithZeroOrMoreChildren = findFirstChildWithZeroOrMoreChildren(rootNode)
        tree.addAll(firstChildWithZeroOrMoreChildren.getChildrenAsList())
        return tree
    }

    private fun findFirstChildWithZeroOrMoreChildren(rootNode: TreeNodeBranch): TreeNodeBranch {
        var currentNode: TreeNodeBranch = rootNode
        while (true) {
            val children = currentNode.getChildrenAsList()
            if (children.size != 1) {
                return currentNode
            }
            currentNode = children[0] as TreeNodeBranch
        }
    }
}
