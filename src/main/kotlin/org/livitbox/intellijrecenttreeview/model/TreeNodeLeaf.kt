package org.livitbox.intellijrecenttreeview.model

import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import java.io.File

class TreeNodeLeaf(
    val projectPath: String
) : AnAction(File(projectPath).name) {

    override fun actionPerformed(e: AnActionEvent) {
        ProjectUtil.openOrImport(projectPath, null, false)
    }

    override fun toString(): String {
        return "TreeNodeLeaf(projectPath='$projectPath')"
    }
}
