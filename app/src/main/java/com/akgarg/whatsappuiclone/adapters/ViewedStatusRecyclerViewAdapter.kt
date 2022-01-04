package com.akgarg.whatsappuiclone.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.models.StatusDataModel
import com.akgarg.whatsappuiclone.viewHolders.StatusRvViewHolder

@Suppress("unused")
class ViewedStatusRecyclerViewAdapter(
    private val context: Context,
    private val viewedStatusData: ArrayList<StatusDataModel>
) :
    RecyclerView.Adapter<StatusRvViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusRvViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.status_rv_layout, parent, false)
        return StatusRvViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StatusRvViewHolder, position: Int) {
        val data = viewedStatusData[position]
        holder.statusUserName.text = data.getStatusTitle()
        holder.statusUploadTime.text = data.getStatusTime()
        holder.profilePicture.setImageResource(data.getStatusProfilePicture())
    }

    override fun getItemCount(): Int {
        return viewedStatusData.size
    }

}