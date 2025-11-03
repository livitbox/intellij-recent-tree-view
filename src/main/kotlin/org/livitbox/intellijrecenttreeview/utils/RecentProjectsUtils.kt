package org.livitbox.intellijrecenttreeview.utils

import com.intellij.ide.RecentProjectListActionProvider
import com.intellij.ide.ReopenProjectAction
import com.intellij.openapi.project.Project
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun getListOfRecentProjectsPaths(project: Project?): List<Path> {
    return RecentProjectListActionProvider
        .getInstance()
        .getActions(addClearListItem = false, useGroups = false)
        .filterIsInstance<ReopenProjectAction>()
        .filter { it.projectPath != project?.basePath }
        .map { Paths.get(it.projectPath) }
}

fun isRecentProjectRemoved(path: Path): Boolean {
    return !Files.exists(path)
}
