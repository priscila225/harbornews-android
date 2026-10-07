package com.harbornews.weather.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ConditionsEntity::class], version = 1, exportSchema = true)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun conditionsDao(): ConditionsDao
}
