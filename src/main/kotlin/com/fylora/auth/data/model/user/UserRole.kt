package com.fylora.auth.data.model.user

sealed class UserRole(val type: String) {
    data object Admin: UserRole("admin")
    data object User: UserRole("user")

    companion object {
        fun fromType(type: String): UserRole =
            when (type) {
                Admin.type -> Admin
                User.type -> User
                else -> User
            }
    }
}