package com.arzaapps.android.yamobile.data.source.remote

import com.arzaapps.android.yamobile.data.entities.ActionCosts
import com.arzaapps.android.yamobile.data.entities.CompanyProfile
import com.arzaapps.android.yamobile.data.entities.IndicesConstituent
import com.arzaapps.android.yamobile.data.source.remote.retrofit.ActionService
import javax.inject.Inject

class ActionsRemoteDataSource @Inject constructor(private val actionService: ActionService) {

    suspend fun getCompanySymbols(): IndicesConstituent {
        return actionService.getCompanySymbols()
    }

    suspend fun getActionCosts(actionSymbol: String): ActionCosts {
        return actionService.getAction(actionSymbol)
    }

    suspend fun getCompanyProfile(actionSymbol: String): CompanyProfile {
        return actionService.getCompanyProfile(actionSymbol)
    }
}