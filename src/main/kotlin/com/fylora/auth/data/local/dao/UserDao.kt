package com.fylora.auth.data.local.dao

import com.fylora.auth.data.model.user.ID
import com.fylora.auth.data.model.user.User

interface UserDao {
    suspend fun getUserById(id: ID): User?
    suspend fun getUserByUsername(username: String): User?
}