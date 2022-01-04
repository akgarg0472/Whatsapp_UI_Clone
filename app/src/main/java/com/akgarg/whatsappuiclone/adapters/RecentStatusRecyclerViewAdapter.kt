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
class RecentStatusRecyclerViewAdapter(
    private val context: Context,
    private val recentStatusData: ArrayList<StatusDataModel>
) :
    RecyclerView.Adapter<StatusRvViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusRvViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.status_rv_layout, parent, false)
        val viewHolder = StatusRvViewHolder(view)

        view.setOnClickListener {
            println("Status clicked ${viewHolder.adapterPosition}")
        }

        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StatusRvViewHolder, position: Int) {
        val data = recentStatusData[position]
        holder.statusUserName.text = data.getStatusTitle()
        holder.statusUploadTime.text = data.getStatusTime()
        holder.profilePicture.setImageResource(data.getStatusProfilePicture())
    }


    override fun getItemCount(): Int {
        return recentStatusData.size
    }

}