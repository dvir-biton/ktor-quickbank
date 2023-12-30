package com.fylora.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val from: String,
    val to: String,
    val date: Long,
    val amount: Long,
    val title: String,
    val message: String
)
