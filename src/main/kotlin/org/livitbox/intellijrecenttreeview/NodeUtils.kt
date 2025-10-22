package org.livitbox.intellijrecenttreeview

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

val ROOT_NAME = "root"

fun createRootNode(path: Path): Node {
    val rootNode = Node(ROOT_NAME, "", null)
    var currentNode: Node? = null
    for (subPath in path.iterateSubpathsWithFull()) {
        val key = subPath.nodeKey()
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

fun addPathAsNode(rootNode: Node, path: Path) {
    var currentNode = rootNode
    for (subPath in path.iterateSubpathsWithFull()) {
        val key = subPath.nodeKey()
        var child = currentNode.getChild(key)
        if (child == null) {
            child = Node(key, subPath.toString(), currentNode);
            currentNode.addChild(child);
        }
        currentNode = child
    }
}

private fun Path.nodeKey(): String {
    if (fileName != null) {
        return fileName.toString()
    }
    return toString().replace(File.separator, "")
}

private fun Path.iterateSubpathsWithFull(): Sequence<Path> = sequence {
    for (i in 0..nameCount) {
        if (i == 0) {
            if (root.toString() != File.separator) {
                yield(root)
            }
            continue
        }
        val fullSubpath = root?.resolve(subpath(0, i)) ?: subpath(0, i)
        yield(fullSubpath)
    }
}
