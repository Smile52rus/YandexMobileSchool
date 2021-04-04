package com.arzaapps.android.yamobile.views.favouritescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.arzaapps.android.yamobile.data.source.ActionsRepository
import com.arzaapps.android.yamobile.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val repository: ActionsRepository
) : ViewModel() {
    fun getFavouriteActions() = liveData(Dispatchers.IO) {
        try {
            emit(Resource.loading(data = null))
            emit(Resource.success(data = repository.getFavouriteActions()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }
}