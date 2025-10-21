package org.livitbox.intellijrecenttreeview

import com.intellij.openapi.options.Configurable
import javax.swing.BoxLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

class RecentProjectsTreeViewConfigurable : Configurable {

    private val settings = RecentProjectsTreeViewSettingsState.instance

    private lateinit var panel: JPanel
    private lateinit var filterRemovedProjectsCheckbox: JCheckBox
    private lateinit var sortTreeBranchesCheckbox: JCheckBox

    override fun getDisplayName(): String = "Recent Projects Tree"

    override fun createComponent(): JComponent {
        panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        filterRemovedProjectsCheckbox = JCheckBox("Filter removed projects", settings.filterRemovedProjects)
        sortTreeBranchesCheckbox = JCheckBox("Sort tree branches elements alphabetically", settings.sortTreeBranches)

        panel.add(filterRemovedProjectsCheckbox)
        panel.add(sortTreeBranchesCheckbox)

        return panel
    }

    override fun isModified(): Boolean {
        return filterRemovedProjectsCheckbox.isSelected != settings.filterRemovedProjects ||
                sortTreeBranchesCheckbox.isSelected != settings.sortTreeBranches
    }

    override fun apply() {
        settings.filterRemovedProjects = filterRemovedProjectsCheckbox.isSelected
        settings.sortTreeBranches = sortTreeBranchesCheckbox.isSelected
    }

    override fun reset() {
        filterRemovedProjectsCheckbox.isSelected = settings.filterRemovedProjects
        sortTreeBranchesCheckbox.isSelected = settings.sortTreeBranches
    }
}
