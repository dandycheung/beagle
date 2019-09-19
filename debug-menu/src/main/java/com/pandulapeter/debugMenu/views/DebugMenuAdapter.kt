package com.pandulapeter.debugMenu.views

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.models.NetworkEvent
import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenu.views.items.header.HeaderViewHolder
import com.pandulapeter.debugMenu.views.items.header.HeaderViewModel
import com.pandulapeter.debugMenu.views.items.logMessage.LogMessageViewHolder
import com.pandulapeter.debugMenu.views.items.logMessage.LogMessageViewModel
import com.pandulapeter.debugMenu.views.items.loggingHeader.LoggingHeaderViewHolder
import com.pandulapeter.debugMenu.views.items.loggingHeader.LoggingHeaderViewModel
import com.pandulapeter.debugMenu.views.items.networkLogEvent.NetworkLogEventViewHolder
import com.pandulapeter.debugMenu.views.items.networkLogEvent.NetworkLogEventViewModel
import com.pandulapeter.debugMenu.views.items.networkLoggingHeader.NetworkLoggingHeaderViewHolder
import com.pandulapeter.debugMenu.views.items.networkLoggingHeader.NetworkLoggingHeaderViewModel
import com.pandulapeter.debugMenu.views.items.settingsLink.SettingsLinkViewHolder
import com.pandulapeter.debugMenu.views.items.settingsLink.SettingsLinkViewModel

internal class DebugMenuAdapter(
    private val textColor: Int,
    private val onSettingsLinkButtonPressed: () -> Unit,
    private val onLoggingHeaderPressed: () -> Unit,
    private val onNetworkLoggingHeaderPressed: () -> Unit,
    private val onNetworkLogEventClicked: (networkEvent: NetworkEvent) -> Unit
) : ListAdapter<DrawerItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<DrawerItem>() {

    override fun areItemsTheSame(oldItem: DrawerItem, newItem: DrawerItem) = oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DrawerItem, newItem: DrawerItem) = oldItem == newItem

}) {
    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is HeaderViewModel -> R.layout.item_header
        is SettingsLinkViewModel -> R.layout.item_settings_link
        is LoggingHeaderViewModel -> R.layout.item_logging_header
        is LogMessageViewModel -> R.layout.item_log_message
        is NetworkLoggingHeaderViewModel -> R.layout.item_network_logging_header
        is NetworkLogEventViewModel -> R.layout.item_network_log_event
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_header -> HeaderViewHolder.create(parent)
        R.layout.item_settings_link -> SettingsLinkViewHolder.create(parent, onSettingsLinkButtonPressed)
        R.layout.item_logging_header -> LoggingHeaderViewHolder.create(parent, onLoggingHeaderPressed)
        R.layout.item_log_message -> LogMessageViewHolder.create(parent)
        R.layout.item_network_logging_header -> NetworkLoggingHeaderViewHolder.create(parent, onNetworkLoggingHeaderPressed)
        R.layout.item_network_log_event -> NetworkLogEventViewHolder.create(parent) { position -> onNetworkLogEventClicked((getItem(position) as NetworkLogEventViewModel).networkEvent) }
        else -> throw IllegalArgumentException("Unsupported view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is HeaderViewHolder -> holder.bind(getItem(position) as HeaderViewModel, textColor)
        is SettingsLinkViewHolder -> holder.bind(getItem(position) as SettingsLinkViewModel, textColor)
        is LoggingHeaderViewHolder -> holder.bind(getItem(position) as LoggingHeaderViewModel, textColor)
        is LogMessageViewHolder -> holder.bind(getItem(position) as LogMessageViewModel, textColor)
        is NetworkLoggingHeaderViewHolder -> holder.bind(getItem(position) as NetworkLoggingHeaderViewModel, textColor)
        is NetworkLogEventViewHolder -> holder.bind(getItem(position) as NetworkLogEventViewModel, textColor)
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }
}