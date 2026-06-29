package edu.swifty.companion

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val login: String,
    @SerialName("displayname")
    val displayName: String,
    val email: String,
    val image: UserImage,
    @SerialName("staff?")
    val isStaff: Boolean,
    @SerialName("alumni?")
    val isAlumni: Boolean,
    @SerialName("active?")
    val isActive: Boolean,
)

@Serializable
data class UserImage(
    val link: String? = null,
)
