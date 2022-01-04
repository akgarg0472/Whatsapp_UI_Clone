package com.akgarg.whatsappuiclone.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.ApplicationConstants
import com.akgarg.whatsappuiclone.models.CallDataModel
import com.akgarg.whatsappuiclone.viewHolders.ChatMessageRvViewHolder

@Suppress("unused")
class SingleChatRecyclerViewAdapter(
    private val context: Context,
    private val messageData: ArrayList<CallDataModel>?
) :
    RecyclerView.Adapter<ChatMessageRvViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageRvViewHolder {
        return when (viewType) {
            ApplicationConstants.MESSAGE_SEND -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.chat_message_layout_send, parent, false)
                ChatMessageRvViewHolder(view)
            }
            else -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.chat_message_layout_received, parent, false)
                ChatMessageRvViewHolder(view)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChatMessageRvViewHolder, position: Int) {
        //val data = messageData[position]

    }


    override fun getItemCount(): Int {
        // return messageData.size
        return 18
    }


    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            ApplicationConstants.MESSAGE_SEND
        } else {
            ApplicationConstants.MESSAGE_RECEIVED
        }
    }


}