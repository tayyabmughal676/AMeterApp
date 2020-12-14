package com.example.ameterapp.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExcelModel::class], version = 1, exportSchema = false)
abstract class ExcelDatabase : RoomDatabase() {

    abstract fun excelDao(): ExcelDAO
    companion object {
        @Volatile
        private var INSTANCE: ExcelDatabase? = null
        fun getDatabase(context: Context): ExcelDatabase {
            val tempsInstance = INSTANCE
            if (tempsInstance != null)
                return tempsInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ExcelDatabase::class.java,
                        "excel_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}