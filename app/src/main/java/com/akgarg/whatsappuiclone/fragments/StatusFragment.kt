package com.akgarg.whatsappuiclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.adapters.RecentStatusRecyclerViewAdapter
import com.akgarg.whatsappuiclone.adapters.ViewedStatusRecyclerViewAdapter
import com.akgarg.whatsappuiclone.utils.StatusDataUtil

class StatusFragment : Fragment() {

    private lateinit var recentStatusRecyclerView: RecyclerView
    private lateinit var viewedStatusRecyclerView: RecyclerView
    private lateinit var recentStatusRecyclerViewAdapter: RecentStatusRecyclerViewAdapter
    private lateinit var viewedStatusRecyclerViewAdapter: ViewedStatusRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.status_fragment, container, false)
        recentStatusRecyclerView = view.findViewById(R.id.recentStatusRecyclerView)
        viewedStatusRecyclerView = view.findViewById(R.id.viewedStatusRecyclerView)
        recentStatusRecyclerViewAdapter =
            RecentStatusRecyclerViewAdapter(requireContext(), StatusDataUtil.getRecentStatusData())
        viewedStatusRecyclerViewAdapter =
            ViewedStatusRecyclerViewAdapter(requireContext(), StatusDataUtil.getRecentStatusData())
        recentStatusRecyclerView.layoutManager = LinearLayoutManager(context)
        viewedStatusRecyclerView.layoutManager = LinearLayoutManager(context)
        recentStatusRecyclerView.adapter = recentStatusRecyclerViewAdapter
        viewedStatusRecyclerView.adapter = viewedStatusRecyclerViewAdapter

        return view
    }

    override fun onResume() {
        super.onResume()
        recentStatusRecyclerView.layoutManager = LinearLayoutManager(context)
        viewedStatusRecyclerView.layoutManager = LinearLayoutManager(context)
        recentStatusRecyclerView.adapter = recentStatusRecyclerViewAdapter
        viewedStatusRecyclerView.adapter = viewedStatusRecyclerViewAdapter
    }

}