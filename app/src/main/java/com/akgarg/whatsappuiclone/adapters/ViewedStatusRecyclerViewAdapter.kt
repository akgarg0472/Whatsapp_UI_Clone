package com.akgarg.whatsappuiclone.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.viewHolders.StatusRvViewHolder

@Suppress("unused")
class ViewedStatusRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<StatusRvViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusRvViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.status_rv_layout, parent, false)
        return StatusRvViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StatusRvViewHolder, position: Int) {

        when (position) {
            0 -> {
                holder.statusUserName.text = "Shubham"
                holder.statusUploadTime.text = "Today, 01:18 pm"
                holder.profilePicture.setImageResource(R.drawable.shubham)
            }
            1 -> {
                holder.statusUserName.text = "Paras"
                holder.statusUploadTime.text = "Today, 04:18 am"
                holder.profilePicture.setImageResource(R.drawable.paras)
            }
            else -> {
                holder.statusUserName.text = "Guruji"
                holder.statusUploadTime.text = "Yesterday, 11:18 pm"
                holder.profilePicture.setImageResource(R.drawable.guruji)
            }
        }
    }

    override fun getItemCount(): Int {
        return 5
    }
}