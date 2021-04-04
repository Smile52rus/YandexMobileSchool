package com.arzaapps.android.yamobile.views.detailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.arzaapps.android.yamobile.data.source.ActionsRepository
import com.arzaapps.android.yamobile.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(val repository: ActionsRepository) : ViewModel() {
    fun getCompanyProfile(ticker: String) = liveData(Dispatchers.IO) {
        try {
            emit(Resource.loading(data = null))
            emit(Resource.success(data = repository.getCompanyProfile(ticker)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }

    fun setFavouriteCompany(ticker: String) = liveData(Dispatchers.IO) {
        try {
            emit(Resource.loading(data = null))
            emit(Resource.success(data = repository.setFavouriteCompany(ticker)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }
}