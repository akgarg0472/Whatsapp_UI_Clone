package com.akgarg.whatsappuiclone.interfaces

import com.akgarg.whatsappuiclone.models.firebase.User

interface IChatClick {

    fun onItemClicked(user: User)

}