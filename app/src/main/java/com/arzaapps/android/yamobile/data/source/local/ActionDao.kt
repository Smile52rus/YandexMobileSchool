package com.arzaapps.android.yamobile.data.source.local

import androidx.room.*
import com.arzaapps.android.yamobile.data.entities.CompanyProfile

@Dao
interface ActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCompanyProfile(list: CompanyProfile)

    @Query("SELECT * FROM company_profiles WHERE ticker LIKE :ticker")
    suspend fun getCompanyProfile(ticker: String): CompanyProfile

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCompanyProfile(companyProfile: CompanyProfile)
}