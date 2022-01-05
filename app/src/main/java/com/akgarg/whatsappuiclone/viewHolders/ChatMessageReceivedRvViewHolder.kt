package com.akgarg.whatsappuiclone.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R

class ChatMessageReceivedRvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var chatMessageReceived: TextView = itemView.findViewById(R.id.chatMessageReceived)
    var chatMessageReceivedTime: TextView = itemView.findViewById(R.id.chatMessageReceivedTime)
}