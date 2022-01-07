package com.akgarg.whatsappuiclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.interfaces.IContactClick
import com.akgarg.whatsappuiclone.models.firebase.User
import com.akgarg.whatsappuiclone.viewHolders.ContactRvViewHolder
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject

@Suppress("unused", "UnnecessaryVariable")
class AllContactsRecyclerViewAdapter(
    private val context: Context,
    private val contactClickListener: IContactClick
) :
    RecyclerView.Adapter<ContactRvViewHolder>() {

    private var contactsData = mutableListOf<DocumentSnapshot>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactRvViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.contact_rv_layout, parent, false)
        val viewHolder = ContactRvViewHolder(view)

        view.setOnClickListener {
            val user = contactsData[viewHolder.adapterPosition].toObject<User>()
            contactClickListener.onItemClicked(user)
        }

        return viewHolder
    }


    override fun onBindViewHolder(holder: ContactRvViewHolder, position: Int) {
        val data = contactsData[position].toObject<User>()

        if (data != null) {
            holder.contactName.text = data.getName()
            holder.contactStatus.text = data.getProfileStatus()
            if (data.getProfilePictureUrl() != null && data.getProfilePictureUrl() != "null") {
                holder.contactProfilePicture.layoutParams.width =
                    ViewGroup.LayoutParams.MATCH_PARENT
                holder.contactProfilePicture.layoutParams.height =
                    ViewGroup.LayoutParams.MATCH_PARENT
                holder.contactProfilePicture.scaleType = ImageView.ScaleType.FIT_XY
                holder.contactProfilePicture.imageTintMode = null
                Glide.with(holder.itemView).load(data.getProfilePictureUrl())
                    .into(holder.contactProfilePicture)
            }
        }
    }


    override fun getItemCount(): Int {
        return contactsData.size
    }


    fun updateContactDataSet(contactsData: MutableList<DocumentSnapshot>) {
        this.contactsData = contactsData
    }

}