package com.akgarg.whatsappuiclone.models

@Suppress("unused")
class ChatDataModel(
    private val chatTitle: String,
    private val chatMessage: String,
    private val lastMessageTime: String,
    private val profilePictureUrl: Int,
    private val isMessageSend: Boolean,
    private val isMessageDelivered: Boolean,
    private val isMessageSeen: Boolean
) {

    fun getChatTitle(): String {
        return this.chatTitle
    }

    fun getChatMessage(): String {
        return this.chatMessage
    }

    fun getLastMessageTime(): String {
        return this.lastMessageTime
    }

    fun getProfilePictureUrl(): Int {
        return this.profilePictureUrl
    }

    fun getIsMessageSend(): Boolean {
        return this.isMessageSend
    }

    fun getIsMessageDelivered(): Boolean {
        return this.isMessageDelivered
    }

    fun getIsMessageSeen(): Boolean {
        return this.isMessageSeen
    }

}