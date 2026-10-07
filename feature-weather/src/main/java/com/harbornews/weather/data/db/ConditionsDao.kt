package com.harbornews.weather.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ConditionsDao {
    @Query("SELECT * FROM conditions WHERE stationId = :stationId")
    suspend fun get(stationId: String): ConditionsEntity?

    @Upsert
    suspend fun upsert(entity: ConditionsEntity)
}
