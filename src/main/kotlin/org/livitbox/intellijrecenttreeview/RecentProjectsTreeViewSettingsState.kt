package org.livitbox.intellijrecenttreeview

import com.intellij.openapi.components.*

@Service(Service.Level.APP)
@State(
    name = "RecentProjectsTreeViewSettingsState",
    storages = [Storage("RecentProjectsTreeViewSettings.xml")]
)
class RecentProjectsTreeViewSettingsState : PersistentStateComponent<RecentProjectsTreeViewSettingsState> {

    var filterRemovedProjects: Boolean = true
    var sortTreeBranches: Boolean = false

    override fun getState(): RecentProjectsTreeViewSettingsState = this

    override fun loadState(state: RecentProjectsTreeViewSettingsState) {
        this.filterRemovedProjects = state.filterRemovedProjects
        this.sortTreeBranches = state.sortTreeBranches
    }

    companion object {
        val instance: RecentProjectsTreeViewSettingsState
            get() = service()
    }
}
