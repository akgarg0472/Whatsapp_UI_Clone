package com.akgarg.whatsappuiclone.interfaces

import com.akgarg.whatsappuiclone.models.firebase.User

interface IContactClick {

    fun onItemClicked(user: User?)
}