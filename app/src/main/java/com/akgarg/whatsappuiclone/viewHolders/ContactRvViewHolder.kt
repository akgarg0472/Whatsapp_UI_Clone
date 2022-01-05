package com.akgarg.whatsappuiclone.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R

class ContactRvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var contactProfilePicture: ImageView = itemView.findViewById(R.id.contactProfilePicture)
    var contactName: TextView = itemView.findViewById(R.id.contactName)
    var contactStatus: TextView = itemView.findViewById(R.id.contactStatus)
}