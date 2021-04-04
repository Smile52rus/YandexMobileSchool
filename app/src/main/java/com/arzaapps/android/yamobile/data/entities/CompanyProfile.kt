package com.arzaapps.android.yamobile.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_profiles")
data class CompanyProfile(
    val country: String,
    val currency: String,
    val exchange: String,
    val ipo: String,
    val marketCapitalization: String,
    val name: String,
    val phone: String,
    val shareOutstanding: String,
    @PrimaryKey
    val ticker: String,
    val weburl: String,
    val logo: String,
    val finnhubIndustry: String,
    var isFavorite: Boolean
)