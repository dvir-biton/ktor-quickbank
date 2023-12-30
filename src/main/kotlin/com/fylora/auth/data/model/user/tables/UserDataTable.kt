package com.fylora.auth.data.model.user.tables

import org.jetbrains.exposed.sql.Table

object UserDataTable: Table() {
    val fullName = varchar("full_name", length = 24)
    val description = varchar("description", length = 128)
    val lastAccessDate = long("last_access_date")
    val amountOfMoney = long("amount_of_money")

    val id = reference("id", UserTable.id)
}