package edu.swifty.companion

import kotlinx.serialization.Serializable

@Serializable
data class UserRoute(val displayName: String)

@Serializable
object Home