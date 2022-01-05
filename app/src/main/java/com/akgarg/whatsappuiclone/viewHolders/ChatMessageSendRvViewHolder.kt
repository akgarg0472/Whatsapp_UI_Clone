package com.akgarg.whatsappuiclone.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R

class ChatMessageSendRvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var chatMessageSend: TextView = itemView.findViewById(R.id.chatMessageSend)
    var chatMessageSendTime: TextView = itemView.findViewById(R.id.chatMessageSendTime)
    var sendMessageStatusTick: ImageView = itemView.findViewById(R.id.sendMessageStatusTick)
}