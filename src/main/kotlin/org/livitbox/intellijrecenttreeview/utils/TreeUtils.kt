package org.livitbox.intellijrecenttreeview.utils

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.diagnostic.Logger
import org.livitbox.intellijrecenttreeview.model.TreeNodeBranch
import org.livitbox.intellijrecenttreeview.model.TreeNodeLeaf
import java.nio.file.Path

val ROOT_NAME = "root"
val UNIX_FILE_SEPARATOR = "/"

private val LOG = Logger.getInstance("#org.livitbox.intellijrecenttreeview.utils.TreeUtils")

fun createRootNode(path: Path): TreeNodeBranch {
    val rootNode = TreeNodeBranch(ROOT_NAME, "", null)
    var currentNode: AnAction = rootNode
    for (pathIteration in path.iterateSubpaths()) {
        val key = pathIteration.subpath.toString()
        val fullPath = pathIteration.fullPath.toString()
        val newNode: AnAction
        if (pathIteration.depth == path.nameCount) {
            newNode = TreeNodeLeaf(fullPath)
            if (currentNode is TreeNodeBranch) {
                currentNode.addChild(key, newNode)
            } else {
                LOG.error("The last node is not a leaf: $fullPath")
                throw RuntimeException("The last node is not a leaf: $fullPath")
            }
        } else {
            newNode = TreeNodeBranch(
                key, fullPath,
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
    for (pathIteration in path.iterateSubpaths()) {
        val key = pathIteration.subpath.toString()
        val fullPath = pathIteration.fullPath.toString()
        if (currentNode !is TreeNodeBranch) {
            continue
        }
        var child: AnAction? = currentNode.getChild(key)
        if (child == null) {
            child = if (pathIteration.depth == path.nameCount) {
                TreeNodeLeaf(fullPath)
            } else {
                TreeNodeBranch(
                    key,
                    fullPath,
                    currentNode
                )
            }
            currentNode.addChild(key, child)
        }
        currentNode = child
    }
}

private fun Path.iterateSubpaths(): Sequence<PathIteration> = sequence {
    for (i in 0..nameCount) {
        if (i == 0) {
            if (root.toString() != UNIX_FILE_SEPARATOR) {
                yield(PathIteration(i, root, root))
            }
            continue
        }
        val subpath = subpath(0, i)
        val lastSubpath = subpath(i - 1, i)
        val fullSubpath = root?.resolve(subpath) ?: subpath
        yield(PathIteration(i, lastSubpath, fullSubpath))
    }
}

class PathIteration(val depth: Int, val subpath: Path, val fullPath: Path)
