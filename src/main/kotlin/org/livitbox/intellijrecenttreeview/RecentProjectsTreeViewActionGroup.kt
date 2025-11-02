package org.livitbox.intellijrecenttreeview

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import org.livitbox.intellijrecenttreeview.settings.RecentProjectsTreeViewSettingsState
import org.livitbox.intellijrecenttreeview.utils.TreeBuilder
import org.livitbox.intellijrecenttreeview.utils.getListOfPrecentProjectsPaths

class RecentProjectsTreeViewActionGroup : ActionGroup("Recent Projects Tree", true) {

    private val treeBuilder: TreeBuilder;
    private val settings = RecentProjectsTreeViewSettingsState.instance

    init {
        treeBuilder = TreeBuilder(settings)
    }

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val currentProject: Project? = e?.project
        val recentProjectsPaths = getListOfPrecentProjectsPaths(currentProject, settings.filterRemovedProjects)
        val tree = treeBuilder.buildTree(currentProject, recentProjectsPaths)
        return tree.toTypedArray()
    }
}
