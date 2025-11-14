package com.jeremy.demo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    /// 查询用户
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    /// 插入用户
    @Insert
    suspend fun insertUser(user: User)

    /// 根据ID更新用户名和密码
    @Query("UPDATE users SET userName = :userName, password = :password WHERE uid = :uid")
    suspend fun updateUserNameAndPasswordById(uid: Int, userName: String, password: String)
}