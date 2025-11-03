package org.livitbox.intellijrecenttreeview.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.platform.ide.progress.withBackgroundProgress
import com.intellij.platform.util.progress.reportProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.livitbox.intellijrecenttreeview.utils.getListOfRecentProjectsPaths
import org.livitbox.intellijrecenttreeview.utils.isRecentProjectRemoved
import org.livitbox.intellijrecenttreeview.utils.showNotification
import java.nio.file.Path

class CheckRemovedRecentProjectsAction(val project: Project) : AnAction("Count removed recent projects") {

    override fun actionPerformed(e: AnActionEvent) {
        val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
        scope.launch {
            val maxProgress = 100
            var removedProjectsCounter = 0
            val removedProjectsPaths = mutableSetOf<String>()
            withBackgroundProgress(project, "Counting removed recent projects", cancellable = true) {
                reportProgress { reporter ->
                    lateinit var recentProjectsPaths: List<Path>
                    reporter.indeterminateStep("Fetching recent projects...") {
                        recentProjectsPaths = getListOfRecentProjectsPaths(e.project)
//                        delay(300)
                    }
                    var percentCounter = 0
                    val step = maxProgress / recentProjectsPaths.size
                    for ((i, path) in recentProjectsPaths.withIndex()) {
                        // if it is the last step then use all remaining percentage
                        val currentStep = if (i < recentProjectsPaths.size - 1) step else maxProgress - percentCounter
                        reporter.sizedStep(currentStep, path.toString()) {
                            if (isRecentProjectRemoved(path)) {
                                removedProjectsCounter++
                                removedProjectsPaths.add(path.toString())
                            }
                            percentCounter += currentStep
//                            delay(300)
                        }
                    }
                }
            }
            val message =
                if (removedProjectsCounter == 0) {
                    "No removed recent projects"
                } else {
                    """$removedProjectsCounter removed recent projects.<br> Projects paths:
${removedProjectsPaths.joinToString("<br>") { it }}"""
                }
            showNotification(project, message)
        }
    }
}
