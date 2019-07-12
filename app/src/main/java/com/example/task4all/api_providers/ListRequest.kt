package com.example.task4all.api_providers

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.task4all.helpers.HandleJson

class ListRequest(context: Context) : BaseRequest() {

    private val TAG = "ListRequest"
    val result = MutableLiveData<List<String>>()

    init {
        status.value = Companion.Status.LOADING
        request(context)
    }

    override fun request(context: Context) {
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(Request.Method.GET, baseUrl, Response.Listener { response ->
            handleResponse(response)
        }, Response.ErrorListener { error ->
            Log.d(TAG, "Response error: $error")
            status.postValue(Companion.Status.ERROR)
        })

        queue.add(stringRequest)
    }

    override fun handleResponse(response: String) {
        val list = HandleJson.handleJsonResult(response)
        if (list == null) {
            Log.d(TAG, "Unable to hand response")
            return
        }
        result.postValue(list)
        status.postValue(Companion.Status.SUCCESS)
    }
}