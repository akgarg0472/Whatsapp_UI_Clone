package com.akgarg.whatsappuiclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.adapters.CallsRecyclerViewAdapter
import com.akgarg.whatsappuiclone.utils.CallDataUtil

class CallsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CallsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.calls_fragment, container, false)
        recyclerView = view.findViewById(R.id.callRecyclerView)
        adapter = CallsRecyclerViewAdapter(requireContext(), CallDataUtil.getCallData())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onResume() {
        super.onResume()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}