package org.livitbox.intellijrecenttreeview

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class TreeBuilderTest() {

    val treeBuilder = TreeBuilder()

    companion object {
        @BeforeAll
        @JvmStatic
        fun setupApplication() {
            // Mock Application
            val mockApp = mock<Application>()
            ApplicationManager.setApplication(mockApp, mock(Disposable::class.java))

            // Mock ActionManager and return it from Application
            val mockActionManager = mock<ActionManager>()
            whenever(mockApp.getService(ActionManager::class.java)).thenReturn(mockActionManager)
        }
    }

    @Test
    fun buildTree() {
        val paths = persistentListOf("/Users/user1/test1", "C:/Users/user1/test1", "D:/Users/user1/test1")

        val result = treeBuilder.buildTree(paths)

        assertNotNull(result)
        assertEquals(3, result.size)
    }
}
