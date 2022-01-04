package com.akgarg.whatsappuiclone.models.firebase

@Suppress("unused")
class ChatMessage(
    private val message: String,
    private val time: ChatMessageTime,
    private val senderUid: String?,
    private val receiverUid: String?,
    private val isMessageDelivered: Boolean,
    private val isMessageSeen: Boolean
) {

    fun getMessage() = this.message

    fun getTime() = this.time

    fun getSenderUid() = this.senderUid

    fun getReceiverUid() = this.receiverUid

    fun getIsMessageDelivered() = this.isMessageDelivered

    fun getIsMessageSeen() = this.isMessageSeen

    override fun toString(): String {
        return "$message send by $senderUid to $receiverUid @ $time"
    }

}