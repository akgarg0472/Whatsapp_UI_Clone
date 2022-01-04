package com.akgarg.whatsappuiclone.models.firebase

@Suppress("unused")
class User
    (
    private val uid: String,
    private val name: String,
    private val profilePictureUrl: String?,
    private val countryCode: Byte,
    private val number: String
) {

    fun getUid() = this.uid

    fun getName() = this.name

    fun getProfilePictureUrl() = this.profilePictureUrl

    fun getCountryCode() = this.countryCode

    fun getNumber() = this.number

}