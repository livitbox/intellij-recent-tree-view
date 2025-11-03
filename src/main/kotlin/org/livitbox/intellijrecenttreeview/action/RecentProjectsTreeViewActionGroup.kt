package org.livitbox.intellijrecenttreeview.action

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import org.livitbox.intellijrecenttreeview.builder.TreeBuilder
import org.livitbox.intellijrecenttreeview.settings.RecentProjectsTreeViewSettingsState
import org.livitbox.intellijrecenttreeview.utils.getListOfRecentProjectsPaths
import org.livitbox.intellijrecenttreeview.utils.isRecentProjectRemoved

class RecentProjectsTreeViewActionGroup : ActionGroup("Recent Projects Tree", true) {

    private val treeBuilder: TreeBuilder;
    private val settings = RecentProjectsTreeViewSettingsState.instance

    init {
        treeBuilder = TreeBuilder(settings)
    }

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val currentProject: Project? = e?.project
        var recentProjectsPaths = getListOfRecentProjectsPaths(currentProject)
        if (settings.filterRemovedProjects) {
            recentProjectsPaths = recentProjectsPaths.filter { !isRecentProjectRemoved(it) }
        }
        val tree = treeBuilder.buildTree(currentProject, recentProjectsPaths)
        return tree.toTypedArray()
    }
}
