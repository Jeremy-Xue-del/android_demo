package com.jeremy.demo.model

import com.google.gson.annotations.SerializedName

data class RealTimeModel(
    val primary: Double,
    val realtime: Realtime
)

data class Realtime(
    @SerializedName("air_quality")
    val airQuality: AirQuality,
    @SerializedName("apparent_temperature")
    val apparentTemperature: Double,
    val cloudrate: Double,
    val dswrf: Double,
    val humidity: Double,
    @SerializedName("life_index")
    val lifeIndex: LifeIndex,
    val precipitation: Precipitation,
    val pressure: Double,
    val skycon: String,
    val status: String,
    val temperature: Double,
    val visibility: Double,
    val wind: Wind
)

data class AirQuality(
    val aqi: Aqi,
    val co: Double,
    val description: Description,
    val no2: Double,
    val o3: Double,
    val pm10: Double,
    val pm25: Double,
    val so2: Double
)

data class LifeIndex(
    val comfort: Comfort,
    val ultraviolet: Ultraviolet
)

data class Precipitation(
    val local: Local,
    val nearest: Nearest
)

data class Wind(
    val direction: Double,
    val speed: Double
)

data class Aqi(
    val chn: Double,
    val usa: Double
)

data class Description(
    val chn: String,
    val usa: String
)

data class Comfort(
    val desc: String,
    val index: Double
)

data class Ultraviolet(
    val desc: String,
    val index: Double
)

data class Local(
    val datasource: String,
    val intensity: Double,
    val status: String
)

data class Nearest(
    val distance: Double,
    val intensity: Double,
    val status: String
)