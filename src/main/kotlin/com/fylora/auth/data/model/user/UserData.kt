package com.fylora.auth.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val fullName: String,
    val description: String,
    val amountOfMoney: Long,
    val lastAccessDate: Long,

    val id: ID? = null,
)
