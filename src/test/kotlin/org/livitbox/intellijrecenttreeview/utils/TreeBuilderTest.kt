package org.livitbox.intellijrecenttreeview.utils

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.livitbox.intellijrecenttreeview.builder.TreeBuilder
import org.livitbox.intellijrecenttreeview.settings.RecentProjectsTreeViewSettingsState
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.nio.file.Paths

class TreeBuilderTest() {

    lateinit var treeBuilder: TreeBuilder
    lateinit var project: Project

    companion object {
        @BeforeAll
        @JvmStatic
        fun setUpAll() {
            // Mock Application
            val mockApp = mock<Application>()
            ApplicationManager.setApplication(mockApp, mock(Disposable::class.java))

            // Mock ActionManager and return it from Application
            val mockActionManager = mock<ActionManager>()
            whenever(mockApp.getService(ActionManager::class.java)).thenReturn(mockActionManager)
        }
    }

    @BeforeEach
    fun setupUp() {
        val mockSettings = mock<RecentProjectsTreeViewSettingsState>()
        this.treeBuilder = TreeBuilder(mockSettings)
        this.project = mock()
    }

    @Test
    fun buildTreeUniqueRoot() {
        val paths = persistentListOf(
            Paths.get("/user1/test1"),
            Paths.get("/user2/test2"),
            Paths.get("/user3/test3")
        )

        val result = treeBuilder.buildTree(paths)

        assertNotNull(result)
        assertEquals(3, result.size)
    }

    @Test
    fun buildTreeSameRoot() {
        val paths = persistentListOf(
            Paths.get("/user1/test1"),
            Paths.get("/user1/test2"),
            Paths.get("/user2/test3")
        )

        val result = treeBuilder.buildTree(paths)

        assertNotNull(result)
        assertEquals(2, result.size)
    }
}
