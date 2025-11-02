package org.livitbox.intellijrecenttreeview.utils

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

fun showNotification(project: Project, message: String) {
    NotificationGroupManager.getInstance()
        .getNotificationGroup("RecentProjectsTreeNotifications")
        .createNotification(message, NotificationType.INFORMATION)
        .notify(project)
}
