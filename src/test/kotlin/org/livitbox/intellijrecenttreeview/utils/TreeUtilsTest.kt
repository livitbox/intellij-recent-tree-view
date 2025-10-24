package org.livitbox.intellijrecenttreeview.utils

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.livitbox.intellijrecenttreeview.model.TreeNodeBranch
import org.livitbox.intellijrecenttreeview.model.TreeNodeLeaf
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.nio.file.Path

class TreeUtilsTest {

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

    @Test
    fun createRootNodeTestUnix() {
        val configuration = Configuration.unix()
        val result = createRootNode(
            createPath(
                configuration,
                "/Users/user1/projects/test1"
            )
        )

        assertNotNull(result)
        assertNull(result.parent)
        assertEquals(ROOT_NAME, result.key)
        assertEquals("", result.fullPath)

        val childDepth1 = result.getChild("Users") as TreeNodeBranch?
        assertNotNull(childDepth1)
        assertEquals(result, childDepth1!!.parent)
        assertEquals("Users", childDepth1.key)
        assertEquals(createPath(configuration, "/Users").toString(), childDepth1.fullPath)

        val childDepth2 = childDepth1.getChild("user1") as TreeNodeBranch?
        assertNotNull(childDepth2)
        assertEquals(childDepth1, childDepth2!!.parent)
        assertEquals("user1", childDepth2.key)
        assertEquals(createPath(configuration, "/Users/user1").toString(), childDepth2.fullPath)

        val childDepth3 = childDepth2.getChild("projects") as TreeNodeBranch?
        assertNotNull(childDepth3)
        assertEquals(childDepth2, childDepth3!!.parent)
        assertEquals("projects", childDepth3.key)
        assertEquals(
            createPath(configuration, "/Users/user1/projects").toString(),
            childDepth3.fullPath
        )

        val childDepth4 = childDepth3.getChild("test1") as TreeNodeLeaf?
        assertNotNull(childDepth4)
    }

    @Test
    fun createRootNodeTestWindows() {
        val configuration = Configuration.windows()
        val result = createRootNode(
            createPath(
                configuration,
                "C:\\Users\\user1\\projects\\test1"
            )
        )

        assertNotNull(result)
        assertNull(result.parent)
        assertEquals(ROOT_NAME, result.key)
        assertEquals("", result.fullPath)

        val childDepth1 = result.getChild("C:\\") as TreeNodeBranch?
        assertNotNull(childDepth1)
        assertEquals(result, childDepth1!!.parent)
        assertEquals("C:\\", childDepth1.key)
        assertEquals(createPath(configuration, "C:\\").toString(), childDepth1.fullPath)

        val childDepth2 = childDepth1.getChild("Users") as TreeNodeBranch?
        assertNotNull(childDepth2)
        assertEquals(childDepth1, childDepth2!!.parent)
        assertEquals("Users", childDepth2.key)
        assertEquals(createPath(configuration, "C:\\Users").toString(), childDepth2.fullPath)

        val childDepth3 = childDepth2.getChild("user1") as TreeNodeBranch?
        assertNotNull(childDepth3)
        assertEquals(childDepth2, childDepth3!!.parent)
        assertEquals("user1", childDepth3.key)
        assertEquals(createPath(configuration, "C:\\Users\\user1").toString(), childDepth3.fullPath)

        val childDepth4 = childDepth3.getChild("projects") as TreeNodeBranch?
        assertNotNull(childDepth4)
        assertEquals(childDepth3, childDepth4!!.parent)
        assertEquals("projects", childDepth4.key)
        assertEquals(createPath(configuration, "C:\\Users\\user1\\projects").toString(), childDepth4.fullPath)

        val childDepth5 = childDepth4.getChild("test1")
        assertNotNull(childDepth5)
    }

    @Test
    fun addPathAsNodeTestUnix() {
        val configuration = Configuration.unix()
        val rootNode = createRootNode(
            createPath(
                configuration,
                "/Users/user1/projects/test1"
            )
        )

        addPathAsNode(
            rootNode,
            createPath(configuration, "/Users/user1/projects/test2")
        )

        val usersGroupNode = rootNode.getChild("Users") as TreeNodeBranch?
        assertNotNull(usersGroupNode)
        val user1GroupNode = usersGroupNode?.getChild("user1") as TreeNodeBranch?
        assertNotNull(user1GroupNode)
        val projectsGroupNode = user1GroupNode?.getChild("projects") as TreeNodeBranch?
        assertNotNull(projectsGroupNode)
        val test1OpenRecentProjectAction = projectsGroupNode?.getChild("test1") as TreeNodeLeaf
        val test2OpenRecentProjectAction = projectsGroupNode.getChild("test2") as TreeNodeLeaf

        assertNotNull(test1OpenRecentProjectAction)
        assertEquals(
            createPath(configuration, "/Users/user1/projects/test1").toString(),
            test1OpenRecentProjectAction.projectPath
        )
        assertNotNull(test2OpenRecentProjectAction)
        assertEquals(
            createPath(configuration, "/Users/user1/projects/test2").toString(),
            test2OpenRecentProjectAction.projectPath
        )
    }

    @Test
    fun addPathAsNodeTestWindows() {
        val configuration = Configuration.windows()
        val rootNode = createRootNode(
            createPath(
                configuration,
                "C:\\Users\\user1\\projects\\test1"
            )
        )

        addPathAsNode(
            rootNode,
            createPath(configuration, "C:\\Users\\user1\\projects\\test2")
        )

        val cGroupNode = rootNode.getChild("C:\\") as TreeNodeBranch?
        assertNotNull(cGroupNode)
        val usersGroupNode = cGroupNode?.getChild("Users") as TreeNodeBranch?
        assertNotNull(usersGroupNode)
        val user1GroupNode = usersGroupNode?.getChild("user1") as TreeNodeBranch?
        assertNotNull(user1GroupNode)
        val projectsGroupNode = user1GroupNode?.getChild("projects") as TreeNodeBranch?
        assertNotNull(projectsGroupNode)
        val test1OpenRecentProjectAction = projectsGroupNode?.getChild("test1") as TreeNodeLeaf
        val test2OpenRecentProjectAction = projectsGroupNode.getChild("test2") as TreeNodeLeaf

        assertNotNull(test1OpenRecentProjectAction)
        assertEquals(
            createPath(configuration, "C:\\Users\\user1\\projects\\test1").toString(),
            test1OpenRecentProjectAction.projectPath
        )
        assertNotNull(test2OpenRecentProjectAction)
        assertEquals(
            createPath(configuration, "C:\\Users\\user1\\projects\\test2").toString(),
            test2OpenRecentProjectAction.projectPath
        )
    }

    private fun createPath(configuration: Configuration, pathStr: String): Path {
        val fs = Jimfs.newFileSystem(configuration)
        return fs.getPath(pathStr);
    }
}
