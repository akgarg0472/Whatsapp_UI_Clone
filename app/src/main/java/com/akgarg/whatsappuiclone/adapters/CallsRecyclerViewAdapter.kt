package com.akgarg.whatsappuiclone.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.models.CallDataModel
import com.akgarg.whatsappuiclone.utils.CallDataUtil
import com.akgarg.whatsappuiclone.viewHolders.CallsRvViewHolder

@Suppress("unused")
class CallsRecyclerViewAdapter(
    private val context: Context,
    private val callData: ArrayList<CallDataModel>
) :
    RecyclerView.Adapter<CallsRvViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallsRvViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.call_rv_layout, parent, false)
        return CallsRvViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CallsRvViewHolder, position: Int) {
        val data = callData[position]
        holder.profileName.text = data.getProfileName()
        holder.callTime.text = data.getCallDateAndTime()
        holder.profilePicture.setImageResource(data.getProfilePicture())
        holder.callDirectionIcon.setImageResource(CallDataUtil.getCallDirectionIcon(data))
        holder.callImageView.setOnClickListener {
            Toast.makeText(context, "503, Can't Place Call", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return callData.size
    }

}