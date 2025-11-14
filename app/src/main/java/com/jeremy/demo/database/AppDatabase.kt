package com.jeremy.demo.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [User::class, Weather::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        // 定义从版本1到版本2的迁移
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 创建新的临时表
                database.execSQL(
                    "CREATE TABLE users_new (uid INTEGER PRIMARY KEY NOT NULL, userName TEXT NOT NULL, password TEXT NOT NULL)"
                )

                // 拷贝旧数据到新表
                database.execSQL(
                    "INSERT INTO users_new (uid, userName, password) SELECT CAST(ROWID AS INTEGER), userName, password FROM users"
                )

                // 删除旧表
                database.execSQL("DROP TABLE users")

                // 将新表重命名为原表名
                database.execSQL("ALTER TABLE users_new RENAME TO users")
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }

        }
    }
}