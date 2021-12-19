package com.akgarg.whatsappuiclone.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R

class CallsRvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var profilePicture: ImageView = itemView.findViewById(R.id.callProfilePicture)
    var profileName: TextView = itemView.findViewById(R.id.callProfileName)
    var callImageView: ImageView = itemView.findViewById(R.id.callIconImage)
    var callTime: TextView = itemView.findViewById(R.id.callDateAndTime)
    var callDirectionIcon: ImageView = itemView.findViewById(R.id.callDirectionIcon)
}