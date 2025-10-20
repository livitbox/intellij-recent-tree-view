package org.livitbox.intellijrecenttreeview

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class NodeUtilsTest {

    @Test
    fun createRootNodeTestUnix() {
        val result = createRootNode(createPathString(true, "Users", "user1", "projects", "test1"))

        assertNotNull(result)
        assertNull(result.getParent())
        assertEquals(ROOT_NAME, result.getKey())
        assertEquals("", result.getFullPath())

        val childDepth1 = result.getChild("Users")
        assertNotNull(childDepth1)
        assertEquals(result, childDepth1!!.getParent())
        assertEquals("Users", childDepth1.getKey())
        assertEquals(createPathString(true, "Users"), childDepth1.getFullPath())

        val childDepth2 = childDepth1.getChild("user1")
        assertNotNull(childDepth2)
        assertEquals(childDepth1, childDepth2!!.getParent())
        assertEquals("user1", childDepth2.getKey())
        assertEquals(createPathString(true, "Users", "user1"), childDepth2.getFullPath())

        val childDepth3 = childDepth2.getChild("projects")
        assertNotNull(childDepth3)
        assertEquals(childDepth2, childDepth3!!.getParent())
        assertEquals("projects", childDepth3.getKey())
        assertEquals(
            createPathString(true, "Users", "user1", "projects"),
            childDepth3.getFullPath()
        )

        val childDepth4 = childDepth3.getChild("test1")
        assertNotNull(childDepth4)
        assertEquals(childDepth3, childDepth4!!.getParent())
        assertEquals("test1", childDepth4.getKey())
        assertEquals(
            createPathString(true, "Users", "user1", "projects", "test1"),
            childDepth4.getFullPath()
        )
    }

    @Test
    fun createRootNodeTestWindows() {
        val result = createRootNode(createPathString(false, "C:", "Users", "user1", "projects", "test1"))

        assertNotNull(result)
        assertNull(result.getParent())
        assertEquals(ROOT_NAME, result.getKey())
        assertEquals("", result.getFullPath())

        val childDepth1 = result.getChild("C:")
        assertNotNull(childDepth1)
        assertEquals(result, childDepth1!!.getParent())
        assertEquals("C:", childDepth1.getKey())
        assertEquals("C:" + File.separator, childDepth1.getFullPath())

        val childDepth2 = childDepth1.getChild("Users")
        assertNotNull(childDepth2)
        assertEquals(childDepth1, childDepth2!!.getParent())
        assertEquals("Users", childDepth2.getKey())
        assertEquals(createPathString(false, "C:", "Users"), childDepth2.getFullPath())

        val childDepth3 = childDepth2.getChild("user1")
        assertNotNull(childDepth3)
        assertEquals(childDepth2, childDepth3!!.getParent())
        assertEquals("user1", childDepth3.getKey())
        assertEquals(createPathString(false, "C:", "Users", "user1"), childDepth3.getFullPath())

        val childDepth4 = childDepth3.getChild("projects")
        assertNotNull(childDepth4)
        assertEquals(childDepth3, childDepth4!!.getParent())
        assertEquals("projects", childDepth4.getKey())
        assertEquals(createPathString(false, "C:", "Users", "user1", "projects"), childDepth4.getFullPath())

        val childDepth5 = childDepth4.getChild("test1")
        assertNotNull(childDepth5)
        assertEquals(childDepth4, childDepth5!!.getParent())
        assertEquals("test1", childDepth5.getKey())
        assertEquals(createPathString(false, "C:", "Users", "user1", "projects", "test1"), childDepth5.getFullPath())
    }

    @Test
    fun addPathAsNodeTestMixed() {
        val rootNode = createRootNode(createPathString(true, "Users", "user1", "projects", "test1"))

        addPathAsNode(rootNode, createPathString(true, "Users", "user1", "projects", "test2"))
        addPathAsNode(rootNode, createPathString(false, "C:", "user2", "projects", "test3"))

        val test2Node = rootNode.getChild("Users")?.getChild("user1")?.getChild("projects")?.getChild("test2")
        val test3Node = rootNode.getChild("C:")?.getChild("user2")?.getChild("projects")?.getChild("test3")

        assertNotNull(test2Node)
        assertEquals("test2", test2Node?.getKey())
        assertEquals(createPathString(true, "Users", "user1", "projects", "test2"), test2Node?.getFullPath())

        assertNotNull(test3Node)
        assertEquals("test3", test3Node?.getKey())
        assertEquals(createPathString(false, "C:", "user2", "projects", "test3"), test3Node?.getFullPath())
    }

    private fun createPathString(hasLeadingFileSeparator: Boolean, vararg pathParts: String): String {
        val path = pathParts.joinToString(File.separator)
        return if (hasLeadingFileSeparator) {
            File.separator + path
        } else {
            path
        }
    }
}
