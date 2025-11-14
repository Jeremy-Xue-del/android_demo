package com.jeremy.demo.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weathers")
data class Weather(
    /// 地理ID
    @PrimaryKey val id: Int,
    /// 地理名称
    val name: String,
    /// 纬度
    val lat: Double,
    /// 经度
    val lon: Double,
)