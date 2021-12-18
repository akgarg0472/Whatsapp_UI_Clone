package com.akgarg.whatsappuiclone.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.viewHolders.ChatRvViewHolder

@Suppress("unused")
class ChatRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<ChatRvViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRvViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_rv_layout, parent, false)
        return ChatRvViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChatRvViewHolder, position: Int) {

        when (position) {
            0 -> {
                holder.chatTitle.text = "Shubham"
                holder.chatMessage.text = "Hello beero"
                holder.lastMessageTime.text = "02:55 am"
                holder.profilePicture.setImageResource(R.drawable.shubham)
            }
            1 -> {
                holder.chatTitle.text = "Paras"
                holder.chatMessage.text = "Hey, Wassap?"
                holder.lastMessageTime.text = "02:55 am"
                holder.profilePicture.setImageResource(R.drawable.paras)
            }
            2 -> {
                holder.chatTitle.text = "Guruji"
                holder.chatMessage.text = "Pranam Guruji"
                holder.lastMessageTime.text = "02:55 am"
                holder.profilePicture.setImageResource(R.drawable.guruji)
            }
            else -> {
                holder.chatTitle.text = "Akhilesh"
                holder.chatMessage.text = "Hey, this is me"
                holder.lastMessageTime.text = "02:55 am"
                holder.profilePicture.setImageResource(R.drawable.me)
            }
        }
    }

    override fun getItemCount(): Int {
        return 4
    }
}