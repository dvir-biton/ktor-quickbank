package com.fylora.auth.data.local.dao.impl

import com.fylora.auth.data.local.dao.CombinedUserDao
import com.fylora.auth.data.local.dao.UserDao
import com.fylora.auth.data.local.dao.UserDataDao
import com.fylora.auth.data.local.database.DatabaseFactory
import com.fylora.auth.data.model.user.*
import com.fylora.auth.data.model.user.tables.UserDataTable
import com.fylora.auth.data.model.user.tables.UserTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class CombinedUserDaoImpl(
    private val userDao: UserDao,
    private val userDataDao: UserDataDao
): CombinedUserDao {
    override suspend fun insertUser(user: User, userData: UserData): Boolean = DatabaseFactory.dbQuery {
        try {
            transaction {
                UserTable.insert {
                    it[username] = user.username
                    it[password] = user.password
                    it[salt] = user.salt
                    it[id] = user.id
                }

                UserDataTable.insert {
                    it[fullName] = userData.fullName
                    it[amountOfMoney] = userData.amountOfMoney
                    it[description] = userData.description
                    it[lastAccessDate] = userData.lastAccessDate
                    it[id] = user.id
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun updateUser(id: ID, user: User, userData: UserData): Boolean = DatabaseFactory.dbQuery {
        try {
            transaction {
                val updatedUserRows = UserTable.update({ UserTable.id eq id }) {
                    it[username] = user.username
                    it[password] = user.password
                    it[salt] = user.salt
                }

                val updatedUserDataRows = UserDataTable.update({ UserDataTable.id eq id }) {
                    it[fullName] = userData.fullName
                    it[amountOfMoney] = userData.amountOfMoney
                }

                updatedUserRows > 0 && updatedUserDataRows > 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getUserWithDetailsById(id: ID): Pair<User, UserData>? = DatabaseFactory.dbQuery {
        val user = userDao.getUserById(id) ?: return@dbQuery null
        val userData = userDataDao.getUserDataById(id) ?: return@dbQuery null

        Pair(user, userData)
    }

    override suspend fun getUserWithDetailsByUsername(username: String): Pair<User, UserData>? = DatabaseFactory.dbQuery {
        val user = userDao.getUserByUsername(username) ?: return@dbQuery null
        val userData = userDataDao.getUserDataById(user.id) ?: return@dbQuery null

        Pair(user, userData)
    }

    override suspend fun transferMoneyByUsername(from: String, to: String, amount: Long): Boolean = DatabaseFactory.dbQuery {
        val fromUserId = userDao.getUserByUsername(from)?.id ?: return@dbQuery false
        val toUserId = userDao.getUserByUsername(to)?.id ?: return@dbQuery false

        try {
            userDataDao.transferMoney(fromUserId, toUserId, amount)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}