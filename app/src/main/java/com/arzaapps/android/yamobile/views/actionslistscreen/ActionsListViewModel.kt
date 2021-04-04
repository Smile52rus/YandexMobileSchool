package com.arzaapps.android.yamobile.views.actionslistscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.arzaapps.android.yamobile.data.source.ActionsRepository
import com.arzaapps.android.yamobile.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ActionsListViewModel @Inject constructor(
    private val repository: ActionsRepository
) : ViewModel() {

    fun getActions() = liveData(Dispatchers.IO) {
        try {
            emit(Resource.loading(data = null))
            emit(Resource.success(data = repository.getActionsData()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error"))
        }
    }
}