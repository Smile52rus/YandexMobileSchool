package com.arzaapps.android.yamobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arzaapps.android.yamobile.data.source.ActionsRepository

class ViewModelFactory(private val actionsRepository: ActionsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ActionsRepository::class.java)
            .newInstance(actionsRepository)
    }
}