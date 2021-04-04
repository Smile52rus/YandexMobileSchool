package com.arzaapps.android.yamobile.data.source.remote.retrofit

import com.arzaapps.android.yamobile.data.entities.ActionCosts
import com.arzaapps.android.yamobile.data.entities.CompanyProfile
import com.arzaapps.android.yamobile.data.entities.IndicesConstituent
import retrofit2.http.GET
import retrofit2.http.Query

interface ActionService {
    @GET("index/constituents?symbol=^DJI&token=c0vplen48v6t383ln040")
    suspend fun getCompanySymbols(): IndicesConstituent

    @GET("quote?token=c0vplen48v6t383ln040")
    suspend fun getAction(@Query("symbol") symbol: String): ActionCosts

    @GET("stock/profile2?token=c0vplen48v6t383ln040")
    suspend fun getCompanyProfile(@Query("symbol") symbol: String): CompanyProfile
}