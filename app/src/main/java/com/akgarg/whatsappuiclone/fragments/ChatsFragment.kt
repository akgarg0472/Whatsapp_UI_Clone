package com.akgarg.whatsappuiclone.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.activities.SingleChatActivity
import com.akgarg.whatsappuiclone.adapters.ChatRecyclerViewAdapter
import com.akgarg.whatsappuiclone.constants.ChatConstants
import com.akgarg.whatsappuiclone.interfaces.IChatClick
import com.akgarg.whatsappuiclone.models.ChatDataModel
import com.akgarg.whatsappuiclone.utils.ChatDataUtil
import com.google.firebase.firestore.CollectionReference

class ChatsFragment : Fragment(), IChatClick {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatRecyclerViewAdapter

    private lateinit var messageCollectionRef: CollectionReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chat_fragment, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        return view
    }


    override fun onResume() {
        super.onResume()

        adapter = ChatRecyclerViewAdapter(requireContext(), ChatDataUtil.getChatData(), this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }


    override fun onItemClicked(chat: ChatDataModel) {
        println("Chat clicked ${chat.getChatTitle()}")
        val chatIntent = Intent(context, SingleChatActivity::class.java)
        val bundle = Bundle()
        bundle.putInt(ChatConstants.CHAT_PROFILE_PICTURE, chat.getProfilePictureUrl())
        bundle.putString(ChatConstants.CHAT_PROFILE_NAME, chat.getChatTitle())
        chatIntent.putExtras(bundle)
        startActivity(chatIntent)
    }


    private fun getExistingChatUsersForCurrentUser() {

    }


}