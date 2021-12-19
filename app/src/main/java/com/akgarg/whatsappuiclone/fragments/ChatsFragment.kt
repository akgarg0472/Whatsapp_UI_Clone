package com.akgarg.whatsappuiclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.adapters.ChatRecyclerViewAdapter
import com.akgarg.whatsappuiclone.utils.ChatDataUtil

class ChatsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

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
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ChatRecyclerViewAdapter(requireContext(), ChatDataUtil.getChatData())
    }


    override fun onStop() {
        super.onStop()
        recyclerView.layoutManager = null
        recyclerView.adapter = null
    }


}