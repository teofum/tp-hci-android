package com.example.tphci.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCredentials(
    var email: String,
    var password: String
)
