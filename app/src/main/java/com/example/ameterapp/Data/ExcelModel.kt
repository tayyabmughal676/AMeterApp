package com.example.ameterapp.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "excel_table")
data class ExcelModel(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        var valueOfX: String? = null,
        var valueOfY: String? = null,
        var valueOfZ: String? = null,
)

