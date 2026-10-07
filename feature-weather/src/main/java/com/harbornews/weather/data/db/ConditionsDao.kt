package com.harbornews.weather.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ConditionsDao {
    @Query("SELECT * FROM conditions WHERE stationId = :stationId")
    suspend fun get(stationId: String): ConditionsEntity?

    @Upsert
    suspend fun upsert(entity: ConditionsEntity)

    @Query("SELECT * FROM conditions WHERE stationId = :stationId")
    fun observe(stationId: String): Flow<ConditionsEntity?>
}
