package com.akgarg.whatsappuiclone.models

class StatusDataModel(
    private val statusTitle: String,
    private val statusTime: String,
    private val statusProfilePicture: Int,
) {

    fun getStatusTitle(): String {
        return this.statusTitle
    }

    fun getStatusTime(): String {
        return this.statusTime
    }

    fun getStatusProfilePicture(): Int {
        return this.statusProfilePicture
    }

}