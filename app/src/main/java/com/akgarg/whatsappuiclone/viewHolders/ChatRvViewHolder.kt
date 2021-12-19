package com.akgarg.whatsappuiclone.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R

class ChatRvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var chatTitle: TextView = itemView.findViewById(R.id.chatTitle)
    var chatMessage: TextView = itemView.findViewById(R.id.chatMessagePreview)
    var lastMessageTime: TextView = itemView.findViewById(R.id.lastMessageTime)
    var profilePicture: ImageView = itemView.findViewById(R.id.profilePicture)
    var messageStatusTick: ImageView = itemView.findViewById(R.id.messageStatusTick)
}
