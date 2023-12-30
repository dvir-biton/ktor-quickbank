package com.fylora.auth.data.local.dao

import com.fylora.auth.data.model.user.ID
import com.fylora.auth.data.model.user.User
import com.fylora.auth.data.model.user.UserData

interface CombinedUserDao {
    suspend fun insertUser(user: User, userData: UserData): Boolean
    suspend fun updateUser(id: ID, user: User, userData: UserData): Boolean
    suspend fun getUserWithDetailsById(id: ID): Pair<User, UserData>?
    suspend fun getUserWithDetailsByUsername(username: String): Pair<User, UserData>?
    suspend fun transferMoneyByUsername(from: String, to: String, amount: Long): Boolean
}