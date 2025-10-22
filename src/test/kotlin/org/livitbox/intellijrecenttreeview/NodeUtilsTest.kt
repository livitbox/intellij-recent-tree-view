package org.livitbox.intellijrecenttreeview

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Path

class NodeUtilsTest {

    @Test
    fun createRootNodeTestUnix() {
        val configuration = Configuration.unix()
        val result = createRootNode(createPath(configuration, "/Users/user1/projects/test1"))

        assertNotNull(result)
        assertNull(result.getParent())
        assertEquals(ROOT_NAME, result.getKey())
        assertEquals("", result.getFullPath())

        val childDepth1 = result.getChild("Users")
        assertNotNull(childDepth1)
        assertEquals(result, childDepth1!!.getParent())
        assertEquals("Users", childDepth1.getKey())
        assertEquals(createPath(configuration, "/Users").toString(), childDepth1.getFullPath())

        val childDepth2 = childDepth1.getChild("user1")
        assertNotNull(childDepth2)
        assertEquals(childDepth1, childDepth2!!.getParent())
        assertEquals("user1", childDepth2.getKey())
        assertEquals(createPath(configuration, "/Users/user1").toString(), childDepth2.getFullPath())

        val childDepth3 = childDepth2.getChild("projects")
        assertNotNull(childDepth3)
        assertEquals(childDepth2, childDepth3!!.getParent())
        assertEquals("projects", childDepth3.getKey())
        assertEquals(
            createPath(configuration, "/Users/user1/projects").toString(),
            childDepth3.getFullPath()
        )

        val childDepth4 = childDepth3.getChild("test1")
        assertNotNull(childDepth4)
        assertEquals(childDepth3, childDepth4!!.getParent())
        assertEquals("test1", childDepth4.getKey())
        assertEquals(
            createPath(configuration, "/Users/user1/projects/test1").toString(),
            childDepth4.getFullPath()
        )
    }

    @Test
    fun createRootNodeTestWindows() {
        val configuration = Configuration.windows()
        val result = createRootNode(createPath(configuration, "C:\\Users\\user1\\projects\\test1"))

        assertNotNull(result)
        assertNull(result.getParent())
        assertEquals(ROOT_NAME, result.getKey())
        assertEquals("", result.getFullPath())

        val childDepth1 = result.getChild("C:\\")
        assertNotNull(childDepth1)
        assertEquals(result, childDepth1!!.getParent())
        assertEquals("C:\\", childDepth1.getKey())
        assertEquals(createPath(configuration, "C:\\").toString(), childDepth1.getFullPath())

        val childDepth2 = childDepth1.getChild("Users")
        assertNotNull(childDepth2)
        assertEquals(childDepth1, childDepth2!!.getParent())
        assertEquals("Users", childDepth2.getKey())
        assertEquals(createPath(configuration, "C:\\Users").toString(), childDepth2.getFullPath())

        val childDepth3 = childDepth2.getChild("user1")
        assertNotNull(childDepth3)
        assertEquals(childDepth2, childDepth3!!.getParent())
        assertEquals("user1", childDepth3.getKey())
        assertEquals(createPath(configuration, "C:\\Users\\user1").toString(), childDepth3.getFullPath())

        val childDepth4 = childDepth3.getChild("projects")
        assertNotNull(childDepth4)
        assertEquals(childDepth3, childDepth4!!.getParent())
        assertEquals("projects", childDepth4.getKey())
        assertEquals(createPath(configuration, "C:\\Users\\user1\\projects").toString(), childDepth4.getFullPath())

        val childDepth5 = childDepth4.getChild("test1")
        assertNotNull(childDepth5)
        assertEquals(childDepth4, childDepth5!!.getParent())
        assertEquals("test1", childDepth5.getKey())
        assertEquals(
            createPath(configuration, "C:\\Users\\user1\\projects\\test1").toString(),
            childDepth5.getFullPath()
        )
    }

    @Test
    fun addPathAsNodeTestUnix() {
        val configuration = Configuration.unix()
        val rootNode = createRootNode(createPath(configuration, "/Users/user1/projects/test1"))

        addPathAsNode(rootNode, createPath(configuration, "/Users/user1/projects/test2"))

        val test1Node = rootNode.getChild("Users")?.getChild("user1")?.getChild("projects")?.getChild("test1")
        val test2Node = rootNode.getChild("Users")?.getChild("user1")?.getChild("projects")?.getChild("test2")

        assertNotNull(test1Node)
        assertEquals("test1", test1Node?.getKey())
        assertEquals(createPath(configuration, "/Users/user1/projects/test1").toString(), test1Node?.getFullPath())
        assertNotNull(test2Node)
        assertEquals("test2", test2Node?.getKey())
        assertEquals(createPath(configuration, "/Users/user1/projects/test2").toString(), test2Node?.getFullPath())
    }

    @Test
    fun addPathAsNodeTestWindows() {
        val configuration = Configuration.windows()
        val rootNode = createRootNode(createPath(configuration, "C:\\Users\\user1\\projects\\test1"))

        addPathAsNode(rootNode, createPath(configuration, "C:\\Users\\user1\\projects\\test2"))

        val test1Node =
            rootNode.getChild("C:\\")?.getChild("Users")?.getChild("user1")?.getChild("projects")?.getChild("test1")
        val test2Node =
            rootNode.getChild("C:\\")?.getChild("Users")?.getChild("user1")?.getChild("projects")?.getChild("test2")

        assertNotNull(test1Node)
        assertEquals("test1", test1Node?.getKey())
        assertEquals(
            createPath(configuration, "C:\\Users\\user1\\projects\\test1").toString(),
            test1Node?.getFullPath()
        )
        assertNotNull(test2Node)
        assertEquals("test2", test2Node?.getKey())
        assertEquals(
            createPath(configuration, "C:\\Users\\user1\\projects\\test2").toString(),
            test2Node?.getFullPath()
        )
    }

    private fun createPath(configuration: Configuration, pathStr: String): Path {
        val fs = Jimfs.newFileSystem(configuration)
        return fs.getPath(pathStr);
    }
}
