package com.example.tphci.data.model

import com.example.tphci.data.network.model.NetworkCategory
import com.example.tphci.data.network.model.NetworkMetadata
import com.example.tphci.data.network.model.NetworkNewCategory
import java.util.Date

data class Category(
    var id: Int?,
    var name: String?,
    var emoji: String?,
    var createdAt: Date?,
    var updatedAt: Date?
) {
    constructor(id: Int) : this(
        id,
        null,
        null,
        null,
        null
    )

    constructor(name: String, emoji: String = "\uD83D\uDCE6") : this(
        null,
        name,
        emoji,
        null,
        null
    )

    fun asNetworkNewModel(): NetworkNewCategory {
        return NetworkNewCategory(
            name = name,
            metadata = NetworkMetadata(emoji!!)
        )
    }

    fun asNetworkModel(): NetworkCategory {
        return NetworkCategory(
            id = id!!,
            name = name,
            metadata = NetworkMetadata(emoji!!)
        )
    }
}