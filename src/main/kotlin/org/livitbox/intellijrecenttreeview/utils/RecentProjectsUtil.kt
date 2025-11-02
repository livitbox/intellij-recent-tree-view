package org.livitbox.intellijrecenttreeview.utils

import com.intellij.ide.RecentProjectListActionProvider
import com.intellij.ide.ReopenProjectAction
import com.intellij.openapi.project.Project
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun getListOfPrecentProjectsPaths(project: Project?, filterRemovedProjects: Boolean): List<Path> {
    return RecentProjectListActionProvider
        .getInstance()
        .getActions(addClearListItem = false, useGroups = false)
        .filterIsInstance<ReopenProjectAction>()
        .filter { it.projectPath != project?.basePath }
        .filter { !filterRemovedProjects || Files.exists(Paths.get(it.projectPath)) }
        .map { Paths.get(it.projectPath) }
        .toList()
}
