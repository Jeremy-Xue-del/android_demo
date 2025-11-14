package com.jeremy.demo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {
    /// 获取天气数据
    @Query("SELECT * FROM weathers WHERE id = :id")
    suspend fun getWeatherById(id: Int): Weather?

    /// 插入天气数据
    @Insert
    suspend fun insertWeather(weather: Weather)
}