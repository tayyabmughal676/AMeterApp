package com.example.ameterapp.Data

import androidx.lifecycle.LiveData
import androidx.room.*

interface ExcelDAO {

    @Query("SELECT * FROM  excel_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<ExcelModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun addExcelValues(excelModel: ExcelModel)

    @Update
     fun updateExcelValues(excelModel: ExcelModel)
}