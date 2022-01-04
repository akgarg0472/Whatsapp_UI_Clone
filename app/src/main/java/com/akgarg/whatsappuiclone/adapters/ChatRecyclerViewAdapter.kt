package com.akgarg.whatsappuiclone.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.interfaces.IChatClick
import com.akgarg.whatsappuiclone.models.ChatDataModel
import com.akgarg.whatsappuiclone.utils.ChatDataUtil
import com.akgarg.whatsappuiclone.viewHolders.ChatRvViewHolder


@Suppress("unused")
class ChatRecyclerViewAdapter(
    private val context: Context?,
    private val chatData: ArrayList<ChatDataModel>,
    private val clickListener: IChatClick
) :
    RecyclerView.Adapter<ChatRvViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRvViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.chat_rv_layout, parent, false)
        val viewHolder = ChatRvViewHolder(view)

        view.setOnClickListener {
            clickListener.onItemClicked(chatData[viewHolder.adapterPosition])
        }

        return viewHolder
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChatRvViewHolder, position: Int) {
        val data = chatData[position]
        holder.chatTitle.text = data.getChatTitle()
        holder.chatMessage.text = data.getChatMessage()
        holder.lastMessageTime.text = data.getLastMessageTime()
        holder.profilePicture.setImageResource(data.getProfilePictureUrl())

        if (data.getIsMessageSend()) {
            holder.messageStatusTick.setImageResource(ChatDataUtil.getMessageStatusTick(data))
        } else {
            holder.messageStatusTick.visibility = View.GONE
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.marginStart = 0
            holder.chatMessage.layoutParams = params
        }
    }


    override fun getItemCount(): Int {
        return chatData.size
    }

}
