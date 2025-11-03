package org.livitbox.intellijrecenttreeview.builder

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import org.livitbox.intellijrecenttreeview.action.CheckRemovedRecentProjectsAction
import org.livitbox.intellijrecenttreeview.model.TreeNodeBranch
import org.livitbox.intellijrecenttreeview.model.TreeNodeLeaf
import org.livitbox.intellijrecenttreeview.settings.RecentProjectsTreeViewSettingsState
import org.livitbox.intellijrecenttreeview.utils.addPathAsNode
import org.livitbox.intellijrecenttreeview.utils.createRootNode
import java.nio.file.Path

class TreeBuilder(val settings: RecentProjectsTreeViewSettingsState) {

    private val logger = logger<TreeBuilder>()

    private val actionManager: ActionManager = ActionManager.getInstance()

    fun buildTree(project: Project?, paths: List<Path>): List<AnAction> {
        val tree = arrayListOf<AnAction>()
        if (paths.isEmpty()) {
            return tree
        }

        val rootNode = createRootNode(paths[0])
        logger.debug("Created root node for path: '${paths[0]}'")
        for (pathStr in paths) {
            addPathAsNode(rootNode, pathStr)
            logger.debug("Added node for path: '$pathStr'")
        }

        if (settings.sortTreeBranches) {
            rootNode.sortChildren(sortTreeBranchesComparator)
        }

        val firstChildWithZeroOrMoreChildren = findFirstChildWithZeroOrMoreChildren(rootNode)
        tree.addAll(firstChildWithZeroOrMoreChildren.getChildren(actionManager))
        if (project != null && ApplicationManager.getApplication().isInternal) {
            tree.add(Separator.getInstance())
            tree.add(CheckRemovedRecentProjectsAction(project))
        }
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

val sortTreeBranchesComparator = Comparator<String> { a, b ->
    String.CASE_INSENSITIVE_ORDER.compare(a, b)
}
