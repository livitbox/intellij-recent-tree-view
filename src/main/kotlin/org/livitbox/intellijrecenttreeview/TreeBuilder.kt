package org.livitbox.intellijrecenttreeview

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.DefaultActionGroup

class TreeBuilder(val settings: RecentProjectsTreeViewSettingsState) {

    private val sortTreeBranchesComparator = Comparator<String> { a, b ->
        String.CASE_INSENSITIVE_ORDER.compare(a, b)
    }

    fun buildTree(paths: List<String>): List<AnAction> {
        val tree = mutableListOf<AnAction>()
        if (paths.isEmpty()) {
            return tree
        }

        val rootNode = createRootNode(paths[0])
        for (pathStr in paths) {
            addPathAsNode(rootNode, pathStr)
        }

        val depthToActionGroupMap = mutableMapOf<Int, MutableMap<String, DefaultActionGroup>>()
        if (settings.sortTreeBranches) {
            rootNode.sortChildren(sortTreeBranchesComparator)
        }
        rootNode.traverse(0) { node, depth ->
            run {
                if (node.childSize() > 0) {
                    var actionGroup: DefaultActionGroup? = null
                    val actionGroupMap = depthToActionGroupMap[depth]
                    if (actionGroupMap != null) {
                        actionGroup = actionGroupMap[node.getKey()]
                    }
                    if (actionGroup == null) {
                        actionGroup = DefaultActionGroup(node.getKey(), true)
                        addActionToTree(depth, tree, actionGroup, depthToActionGroupMap, node)
                    }
                    if (depthToActionGroupMap[depth] == null) {
                        depthToActionGroupMap[depth] = mutableMapOf()
                    }
                    depthToActionGroupMap[depth]?.put(node.getKey(), actionGroup)
                } else {
                    val openRecentProjectAction = OpenRecentProjectAction(node.getFullPath())
                    addActionToTree(depth, tree, openRecentProjectAction, depthToActionGroupMap, node)
                }
            }
        }

        return filterTreeEmptyBranches(tree)
    }

    private fun addActionToTree(
        depth: Int,
        tree: MutableList<AnAction>,
        anAction: AnAction,
        depthToActionGroupMap: Map<Int, Map<String, DefaultActionGroup>>,
        node: Node
    ) {
        if (depth == 0) {
            tree.add(anAction)
        } else {
            val parentKey = node.getParent()!!.getKey()
            val group =
                findDefaultActionGroup(depthToActionGroupMap, depth - 1, parentKey)
            if (group != null) {
                group.add(anAction)
            } else {
                throw RuntimeException("Parent group not found for key '$parentKey'")
            }
        }
    }

    private fun findDefaultActionGroup(
        depthToActionGroupMap: Map<Int, Map<String, DefaultActionGroup>>,
        depth: Int,
        key: String,
    ): DefaultActionGroup? {
        val map = depthToActionGroupMap[depth]
        return map?.get(key)
    }

    private fun filterTreeEmptyBranches(tree: MutableList<AnAction>): MutableList<AnAction> {
        if (tree.size != 1) {
            return tree
        }
        if (tree[0] !is DefaultActionGroup) {
            return tree
        }
        var children: MutableList<AnAction>
        var currentAction: AnAction = tree[0] as DefaultActionGroup
        while (true) {
            children = (currentAction as DefaultActionGroup).getChildren(ActionManager.getInstance()).toMutableList()
            if (children.size != 1) {
                break
            }
            if (children[0] is OpenRecentProjectAction) {
                break
            }
            currentAction = children[0] as DefaultActionGroup
        }

        return children
    }
}
