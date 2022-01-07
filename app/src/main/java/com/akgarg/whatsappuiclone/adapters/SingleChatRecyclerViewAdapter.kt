package com.akgarg.whatsappuiclone.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.ApplicationConstants
import com.akgarg.whatsappuiclone.models.firebase.ChatMessage
import com.akgarg.whatsappuiclone.utils.SecurityUtils
import com.akgarg.whatsappuiclone.viewHolders.ChatMessageReceivedRvViewHolder
import com.akgarg.whatsappuiclone.viewHolders.ChatMessageSendRvViewHolder
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

@Suppress("unused")
class SingleChatRecyclerViewAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatMessagesData: ArrayList<ChatMessage> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ApplicationConstants.MESSAGE_SEND -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.chat_message_layout_send, parent, false)
                ChatMessageSendRvViewHolder(view)
            }
            else -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.chat_message_layout_received, parent, false)
                ChatMessageReceivedRvViewHolder(view)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatMessage = chatMessagesData[position]

        if (holder.itemViewType == ApplicationConstants.MESSAGE_SEND) {
            val sendMessageViewHolder = holder as ChatMessageSendRvViewHolder

            sendMessageViewHolder.chatMessageSend.text = SecurityUtils.decryptMessage(
                chatMessage.getMessage(),
                chatMessage.getSenderUid(),
                chatMessage.getReceiverUid()
            )
            sendMessageViewHolder.chatMessageSendTime.text =
                chatMessage.getChatMessageTime().toString()

            when {
                chatMessage.getIsMessageSeen() -> {
                    Glide.with(holder.itemView).load(R.drawable.blue_tick)
                        .into(sendMessageViewHolder.sendMessageStatusTick)
                }
                chatMessage.getIsMessageDelivered() -> {
                    Glide.with(holder.itemView).load(R.drawable.double_tick)
                        .into(sendMessageViewHolder.sendMessageStatusTick)
                }
                else -> {
                    Glide.with(holder.itemView).load(R.drawable.single_tick)
                        .into(sendMessageViewHolder.sendMessageStatusTick)
                }
            }
        } else if (holder.itemViewType == ApplicationConstants.MESSAGE_RECEIVED) {
            val receivedMessageViewHolder = holder as ChatMessageReceivedRvViewHolder
            receivedMessageViewHolder.chatMessageReceived.text = SecurityUtils.decryptMessage(
                chatMessage.getMessage(),
                chatMessage.getSenderUid(),
                chatMessage.getReceiverUid()
            )
            receivedMessageViewHolder.chatMessageReceivedTime.text =
                chatMessage.getChatMessageTime().toString()
        }
    }


    override fun getItemCount(): Int {
        return chatMessagesData.size
    }


    override fun getItemViewType(position: Int): Int {
        val message = chatMessagesData[position]

        return if (message.getSenderUid() == FirebaseAuth.getInstance().currentUser?.uid) {
            ApplicationConstants.MESSAGE_SEND
        } else {
            ApplicationConstants.MESSAGE_RECEIVED
        }
    }


    fun updateChatMessageDataset(chatMessagesData: ArrayList<ChatMessage>) {
        this.chatMessagesData = chatMessagesData
    }

}