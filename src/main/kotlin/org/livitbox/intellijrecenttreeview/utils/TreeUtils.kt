package org.livitbox.intellijrecenttreeview.utils

import com.intellij.openapi.actionSystem.AnAction
import org.livitbox.intellijrecenttreeview.model.TreeNodeBranch
import org.livitbox.intellijrecenttreeview.model.TreeNodeLeaf
import java.io.File
import java.nio.file.Path

val ROOT_NAME = "root"

fun createRootNode(path: Path): TreeNodeBranch {
    val rootNode = TreeNodeBranch(ROOT_NAME, "", null)
    var currentNode: AnAction = rootNode
    for ((i, subPath) in path.iterateSubpathsWithFull()) {
        val key = subPath.nodeKey()
        val newNode: AnAction
        if (i == path.nameCount) {
            newNode = TreeNodeLeaf(subPath.toString())
            (currentNode as TreeNodeBranch).addChild(key, newNode)
        } else {
            newNode = TreeNodeBranch(
                key, subPath.toString(),
                currentNode as TreeNodeBranch?
            )
            currentNode.addChild(key, newNode)
            currentNode = newNode
        }
    }
    return rootNode
}

fun addPathAsNode(rootNode: TreeNodeBranch, path: Path) {
    var currentNode: AnAction = rootNode
    for ((i, subPath) in path.iterateSubpathsWithFull()) {
        val key = subPath.nodeKey()
        if (currentNode !is TreeNodeBranch) {
            continue
        }
        var child: AnAction? = currentNode.getChild(key)
        if (child == null) {
            if (i == path.nameCount) {
                child =
                    TreeNodeLeaf(subPath.toString())
            } else {
                child = TreeNodeBranch(
                    key,
                    subPath.toString(),
                    currentNode
                )
            }
            currentNode.addChild(key, child)
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

private fun Path.iterateSubpathsWithFull(): Sequence<Pair<Int, Path>> = sequence {
    for (i in 0..nameCount) {
        if (i == 0) {
            if (root.toString() != File.separator) {
                yield(i to root)
            }
            continue
        }
        val fullSubpath = root?.resolve(subpath(0, i)) ?: subpath(0, i)
        yield(i to fullSubpath)
    }
}
