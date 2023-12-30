package com.fylora.auth.data.local.dao

import com.fylora.auth.data.model.user.ID
import com.fylora.auth.data.model.user.UserData

interface UserDataDao {
    suspend fun getUserDataById(id: ID): UserData?

    suspend fun changeFullNameToId(id: ID, newName: String): Boolean

    suspend fun changeLastAccessTimeToId(id: ID, newLastAccessTime: Long): Boolean

    // money transfer methods
    suspend fun addMoneyToId(id: ID, amountToAdd: Long): Boolean
    suspend fun subtractMoneyToId(id: ID, amountToSubtract: Long): Boolean
    suspend fun updateMoneyToId(id: ID, amount: Long): Boolean
    suspend fun transferMoney(from: ID, to: ID, amount: Long): Boolean
}