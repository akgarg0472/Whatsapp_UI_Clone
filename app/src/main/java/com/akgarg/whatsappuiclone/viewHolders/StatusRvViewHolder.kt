package com.akgarg.whatsappuiclone.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R

class StatusRvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var statusUserName: TextView = itemView.findViewById(R.id.statusUploaderUserName)
    var statusUploadTime: TextView = itemView.findViewById(R.id.statusUploadTime)
    var profilePicture: ImageView = itemView.findViewById(R.id.statusProfilePicture)
}
