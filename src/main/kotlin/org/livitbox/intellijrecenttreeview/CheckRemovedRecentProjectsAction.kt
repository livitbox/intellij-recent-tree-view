package org.livitbox.intellijrecenttreeview

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.platform.ide.progress.withBackgroundProgress
import com.intellij.platform.util.progress.reportProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.livitbox.intellijrecenttreeview.utils.getListOfPrecentProjectsPaths
import org.livitbox.intellijrecenttreeview.utils.showNotification
import java.nio.file.Files
import java.nio.file.Path

class CheckRemovedRecentProjectsAction(val project: Project) : AnAction("Count removed recent projects") {

    override fun actionPerformed(e: AnActionEvent) {
        val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
        scope.launch {
            var removedProjects = 0
            withBackgroundProgress(project, "Counting removed recent projects", cancellable = true) {
                reportProgress { reporter ->
                    var listOfPrecentProjectsPaths: List<Path> = listOf()
                    reporter.indeterminateStep("Fetching all recent projects...") {
                        listOfPrecentProjectsPaths = getListOfPrecentProjectsPaths(e.project, false)
                    }
                    var percentCounter = 0
                    var counter = 0.0;
                    val step = 100 / listOfPrecentProjectsPaths.size
                    for (path in listOfPrecentProjectsPaths) {
                        if (!Files.exists(path)) {
                            removedProjects++
                        }
                        counter += step
                        if (percentCounter < counter) {
                            var progress = counter.toInt() - percentCounter
                            while (progress > 0) {
                                reporter.itemStep {
                                    percentCounter++
                                    progress--
                                }
                            }
                        }
                    }
                    var leftoverProgress = 100 - percentCounter
                    if (leftoverProgress > 0) {
                        while (leftoverProgress > 0) {
                            reporter.itemStep {
                                percentCounter++
                                leftoverProgress--
                            }
                        }
                    }
                }
            }
            val message =
                if (removedProjects == 0) "No removed recent projects" else "$removedProjects removed recent projects"
            showNotification(project, message)
        }
    }
}
