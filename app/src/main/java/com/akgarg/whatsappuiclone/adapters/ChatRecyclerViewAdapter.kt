package com.akgarg.whatsappuiclone.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.interfaces.IChatClick
import com.akgarg.whatsappuiclone.models.firebase.User
import com.akgarg.whatsappuiclone.viewHolders.ChatRvViewHolder
import com.bumptech.glide.Glide


@Suppress("unused")
class ChatRecyclerViewAdapter(
    private val context: Context?,
    private val clickListener: IChatClick
) :
    RecyclerView.Adapter<ChatRvViewHolder>() {

    private var chatUsersData: ArrayList<User> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRvViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.chat_rv_layout, parent, false)
        val viewHolder = ChatRvViewHolder(view)

        view.setOnClickListener {
            clickListener.onItemClicked(chatUsersData[viewHolder.adapterPosition])
        }

        return viewHolder
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChatRvViewHolder, position: Int) {
        val user = chatUsersData[position]
        holder.chatTitle.text = user.getName()
        holder.chatMessage.text = "+${user.getCountryCode()} ${user.getMobileNumber()} "
        holder.lastMessageTime.text = "Tap to start chatting"

        if (user.getProfilePictureUrl() != "null" && user.getProfilePictureUrl() != null) {
            holder.profilePicture.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            holder.profilePicture.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            holder.profilePicture.scaleType = ImageView.ScaleType.CENTER_CROP
            holder.profilePicture.imageTintMode = null
            Glide.with(holder.itemView).load(user.getProfilePictureUrl())
                .into(holder.profilePicture)
        }
    }


    override fun getItemCount(): Int {
        return chatUsersData.size
    }


    fun updateChatDataSet(chatUsersData: ArrayList<User>) {
        this.chatUsersData = chatUsersData
    }


}
