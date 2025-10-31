package org.livitbox.intellijrecenttreeview.utils

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.diagnostic.logger
import org.livitbox.intellijrecenttreeview.model.TreeNodeBranch
import org.livitbox.intellijrecenttreeview.model.TreeNodeLeaf
import org.livitbox.intellijrecenttreeview.settings.RecentProjectsTreeViewSettingsState
import java.nio.file.Path

class TreeBuilder(val settings: RecentProjectsTreeViewSettingsState) {

    private val LOG = logger<TreeBuilder>()

    private val actionManager: ActionManager = ActionManager.getInstance()

    private val sortTreeBranchesComparator = Comparator<String> { a, b ->
        String.CASE_INSENSITIVE_ORDER.compare(a, b)
    }

    fun buildTree(paths: List<Path>): List<AnAction> {
        val tree = arrayListOf<AnAction>()
        if (paths.isEmpty()) {
            return tree
        }

        val rootNode = createRootNode(paths[0])
        LOG.debug("Created root node for path: '${paths[0]}'")
        for (pathStr in paths) {
            addPathAsNode(rootNode, pathStr)
            LOG.debug("Added node for path: '$pathStr'")
        }

        if (settings.sortTreeBranches) {
            rootNode.sortChildren(sortTreeBranchesComparator)
        }

        val firstChildWithZeroOrMoreChildren = findFirstChildWithZeroOrMoreChildren(rootNode)
        tree.addAll(firstChildWithZeroOrMoreChildren.getChildren(actionManager))
        return tree
    }

    private fun findFirstChildWithZeroOrMoreChildren(rootNode: TreeNodeBranch): TreeNodeBranch {
        var currentNode: TreeNodeBranch = rootNode
        while (true) {
            val children = currentNode.getChildren(actionManager)
            if (children.size != 1) {
                return currentNode
            }
            if (children[0] is TreeNodeLeaf) {
                return currentNode
            }
            currentNode = children[0] as TreeNodeBranch
        }
    }
}
