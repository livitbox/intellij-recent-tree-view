package org.livitbox.intellijrecenttreeview.utils

import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

fun showNotification(project: Project, message: String, action: NotificationAction? = null) {
    val notification = NotificationGroupManager.getInstance()
        .getNotificationGroup("RecentProjectsTreeNotifications")
        .createNotification(message, NotificationType.INFORMATION)
    if (action != null) {
        notification.addAction(action)
    }
    notification.notify(project)
}
