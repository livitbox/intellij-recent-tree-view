package org.livitbox.intellijrecenttreeview

import java.nio.file.Path
import java.nio.file.Paths

val ROOT_NAME = "root"

fun createRootNode(pathStr: String): Node {
    val rootNode = Node(ROOT_NAME, "", null)
    var currentNode: Node? = null
    val path = Paths.get(pathStr);
    for (subPath in path.iterateSubpathsWithFull()) {
        val key = subPath.fileName.toString()
        val newNode = Node(key, subPath.toString(), currentNode ?: rootNode)
        if (currentNode == null) {
            rootNode.addChild(newNode)
            currentNode = newNode
        } else {
            currentNode.addChild(newNode)
            currentNode = newNode
        }
    }
    return rootNode
}

fun addPathAsNode(rootNode: Node, path: String) {
    var currentNode = rootNode
    val path = Paths.get(path);
    for (subPath in path.iterateSubpathsWithFull()) {
        val key = subPath.fileName.toString()
        var child = currentNode.getChild(key)
        if (child == null) {
            child = Node(key, subPath.toString(), currentNode);
            currentNode.addChild(child);
        }
        currentNode = child
    }
}

private fun Path.iterateSubpathsWithFull(): Sequence<Path> = sequence {
    for (i in 1..nameCount) {
        // Reattach root if it exists ("/" or "C:\")
        val fullSubpath = root?.resolve(subpath(0, i)) ?: subpath(0, i)
        yield(fullSubpath)
    }
}
