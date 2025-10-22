package org.livitbox.intellijrecenttreeview

import com.intellij.ide.RecentProjectListActionProvider
import com.intellij.ide.ReopenProjectAction
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import java.nio.file.Files
import java.nio.file.Paths


class RecentProjectsTreeViewActionGroup : ActionGroup("Recent Projects Tree", true) {

    private val treeBuilder: TreeBuilder;
    private val settings = RecentProjectsTreeViewSettingsState.instance

    init {
        treeBuilder = TreeBuilder(settings)
    }

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val currentProject: Project? = e?.project
        val recentProjectsPaths = RecentProjectListActionProvider
            .getInstance()
            .getActions(addClearListItem = false, useGroups = false)
            .filterIsInstance<ReopenProjectAction>()
            .filter { it.projectPath != currentProject?.basePath }
            .filter { !settings.filterRemovedProjects || Files.exists(Paths.get(it.projectPath)) }
            .map { Paths.get(it.projectPath) }
            .toList()
        val tree = treeBuilder.buildTree(recentProjectsPaths)
        return tree.toTypedArray()
    }
}
