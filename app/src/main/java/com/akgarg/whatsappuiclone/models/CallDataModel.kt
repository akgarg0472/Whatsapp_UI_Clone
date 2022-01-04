package com.akgarg.whatsappuiclone.models

@Suppress("unused")
class CallDataModel(
    private val profileName: String,
    private val callDateAndTime: String,
    private val profilePicture: Int,
    private val isReceived: Boolean,
    private val isReceivedRejected: Boolean,
    private val isReceivedAccepted: Boolean,
    private val isTransmitted: Boolean,
    private val isTransmittedRejected: Boolean,
    private val isTransmittedAccepted: Boolean
) {

    fun getProfileName(): String {
        return this.profileName
    }

    fun getCallDateAndTime(): String {
        return this.callDateAndTime
    }

    fun getProfilePicture(): Int {
        return this.profilePicture
    }

    fun getIsReceived(): Boolean {
        return this.isReceived
    }

    fun getIsReceivedRejected(): Boolean {
        return this.isReceivedRejected
    }

    fun getIsReceivedAccepted(): Boolean {
        return this.isReceivedAccepted
    }

    fun getIsTransmitted(): Boolean {
        return this.isTransmitted
    }

    fun getIsTransmittedRejected(): Boolean {
        return this.isTransmittedRejected
    }


    fun getIsTransmittedAccepted(): Boolean {
        return this.isTransmittedAccepted
    }

}