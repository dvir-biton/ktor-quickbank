package com.fylora.auth.data.local.dao.impl

import com.fylora.auth.data.local.dao.UserDataDao
import com.fylora.auth.data.local.database.DatabaseFactory
import com.fylora.auth.data.model.user.ID
import com.fylora.auth.data.model.user.UserData
import com.fylora.auth.data.model.user.tables.UserDataTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.minus
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class UserDataDaoImpl: UserDataDao {
    override suspend fun getUserDataById(id: ID): UserData? = DatabaseFactory.dbQuery {
        UserDataTable
            .select { UserDataTable.id eq id }
            .map(::rowToUserData)
            .singleOrNull()
    }

    override suspend fun changeLastAccessTimeToId(id: ID, newLastAccessTime: Long): Boolean = DatabaseFactory.dbQuery {
        UserDataTable
            .update({ UserDataTable.id eq id })  {
                it[lastAccessDate] = newLastAccessTime
            } > 0
    }

    override suspend fun changeFullNameToId(id: ID, newName: String): Boolean = DatabaseFactory.dbQuery {
        UserDataTable
            .update({ UserDataTable.id eq id }) {
                it[fullName] = newName
            } > 0
    }

    override suspend fun addMoneyToId(id: ID, amountToAdd: Long): Boolean = DatabaseFactory.dbQuery {
        UserDataTable
            .update({ UserDataTable.id eq id }) {
                it[amountOfMoney] = amountOfMoney + amountToAdd
            } > 0
    }

    override suspend fun subtractMoneyToId(id: ID, amountToSubtract: Long): Boolean = DatabaseFactory.dbQuery {
        UserDataTable
            .update({ UserDataTable.id eq id }) {
                it[amountOfMoney] = amountOfMoney - amountToSubtract
            } > 0
    }

    override suspend fun updateMoneyToId(id: ID, amount: Long): Boolean = DatabaseFactory.dbQuery {
        UserDataTable
            .update({ UserDataTable.id eq id }) {
                it[amountOfMoney] = amount
            } > 0
    }

    override suspend fun transferMoney(from: ID, to: ID, amount: Long): Boolean = DatabaseFactory.dbQuery {
        try {
            transaction {
                UserDataTable.update({ UserDataTable.id eq from }) {
                    with(SqlExpressionBuilder) {
                        it.update(amountOfMoney, amountOfMoney - amount)
                    }
                }

                UserDataTable.update({ UserDataTable.id eq to }) {
                    with(SqlExpressionBuilder) {
                        it.update(amountOfMoney, amountOfMoney + amount)
                    }
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun rowToUserData(row: ResultRow) = UserData(
        fullName = row[UserDataTable.fullName],
        amountOfMoney = row[UserDataTable.amountOfMoney],
        description = row[UserDataTable.description],
        lastAccessDate = row[UserDataTable.lastAccessDate],
        id = row[UserDataTable.id]
    )
}