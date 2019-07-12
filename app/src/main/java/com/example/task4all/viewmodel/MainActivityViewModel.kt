package com.example.task4all.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.example.task4all.api_providers.BaseRequest
import com.example.task4all.api_providers.ListRequest

class MainActivityViewModel : ViewModel() {

    private lateinit var request: ListRequest

    val options: MutableLiveData<List<String>>
        get() = request.result

    val status: MutableLiveData<BaseRequest.Companion.Status>
        get() = request.status

    fun init(context: Context) {
        request = ListRequest(context)
    }
}