package com.example.sleepbit2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface SleepDao {
    @Query("SELECT * FROM sleep_table")
    fun getAll(): Flow<List<SleepEntity>>

    @Query("SELECT hours_slept FROM sleep_table")
    fun getHoursSlept(): MutableList<String>

    @Insert
    fun insertAll(sleeps: List<SleepEntity>)

    @Insert
    fun insert(sleep: SleepEntity)

    @Query ("DELETE from sleep_table")
    fun deleteAll()


}