package com.arzaapps.android.yamobile.data.source

import com.arzaapps.android.yamobile.data.entities.ActionCosts
import com.arzaapps.android.yamobile.data.entities.ActionData
import com.arzaapps.android.yamobile.data.entities.CompanyProfile
import com.arzaapps.android.yamobile.data.entities.IndicesConstituent
import com.arzaapps.android.yamobile.data.source.local.ActionDao
import com.arzaapps.android.yamobile.data.source.remote.ActionsRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActionsRepository @Inject constructor(
    private val actionsRemoteDataSource: ActionsRemoteDataSource,
    private val actionsLocalDataSource: ActionDao
) {
    var actionData = mutableListOf<ActionData>()

    lateinit var indicesConstituent: IndicesConstituent

    suspend fun getActionsData(): List<ActionData> {
        if (actionData.isEmpty()) {

            indicesConstituent = actionsRemoteDataSource.getCompanySymbols()
            var companyProfile: CompanyProfile? = null
            var actionCosts: ActionCosts? = null

            for (i in 0..10) {
                companyProfile =
                    actionsLocalDataSource.getCompanyProfile(indicesConstituent.constituents[i])
                val job = CoroutineScope(Dispatchers.IO).launch {
                    async {
                        actionCosts =
                            actionsRemoteDataSource.getActionCosts(indicesConstituent.constituents[i])
                    }
                    async {
                        if (companyProfile == null) {
                            actionsLocalDataSource.saveCompanyProfile(
                                actionsRemoteDataSource.getCompanyProfile(
                                    indicesConstituent.constituents[i]
                                )
                            )
                            companyProfile =
                                actionsLocalDataSource.getCompanyProfile(indicesConstituent.constituents[i])
                        }
                    }
                }
                job.join()

                if (!companyProfile?.country.isNullOrEmpty() && !actionCosts?.c.isNullOrEmpty())
                    actionData.add(ActionData(actionCosts!!, companyProfile!!))
            }
        } else {
            actionData = refreshActionData(actionData)
        }
        return actionData
    }

    suspend fun getCompanyProfile(ticker: String): CompanyProfile {
        return actionsLocalDataSource.getCompanyProfile(ticker)
    }

    suspend fun setFavouriteCompany(ticker: String): Boolean {
        val companyProfile = getCompanyProfile(ticker)
        companyProfile.isFavorite = !companyProfile.isFavorite
        actionsLocalDataSource.updateCompanyProfile(companyProfile)
        return companyProfile.isFavorite
    }

    suspend fun getFavouriteActions(): MutableList<ActionData> {
        val favouriteActionData = mutableListOf<ActionData>()
        actionData = refreshActionData(actionData)
        for (action in actionData) {
            if (action.companyProfile.isFavorite) {
                favouriteActionData.add(action)
            }
        }
        return favouriteActionData
    }

    private suspend fun refreshActionData(oldActionData: MutableList<ActionData>): MutableList<ActionData> {
        val newActionData = mutableListOf<ActionData>()
        for (data in oldActionData) {
            newActionData.add(
                ActionData(
                    data.actionCosts,
                    getCompanyProfile(data.companyProfile.ticker)
                )
            )
        }
        return newActionData
    }

}