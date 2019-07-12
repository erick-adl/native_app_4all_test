package com.example.task4all.api_providers

import android.arch.lifecycle.MutableLiveData
import android.content.Context

abstract class BaseRequest {

    val status = MutableLiveData<Status>()

    /***
     * Params: @context to use Volley
     * Function: Uses Volley to perform a http request
     */
    protected abstract fun request(context: Context)

    /***
     * Params: @response JSON string with response
     * Function: Map JSON to Object, then post it on a MutableLiveData<>
     */
    protected abstract fun handleResponse(response: String)

    companion object {
        val baseUrl = "http://dev.4all.com:3003/tarefa/"

        enum class Status {
            SUCCESS,
            LOADING,
            ERROR
        }
    }
}