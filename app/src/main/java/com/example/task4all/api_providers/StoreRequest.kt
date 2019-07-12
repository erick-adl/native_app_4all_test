package com.example.task4all.api_providers

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.task4all.model.Tarefa
import com.example.task4all.helpers.HandleJson

class StoreRequest(context: Context, id: String) : BaseRequest() {

    private val url = baseUrl + id
    private val TAG = "StoreRequest"

    val result = MutableLiveData<Tarefa>()

    init {
        status.value = Companion.Status.LOADING
        request(context)
    }

    override fun request(context: Context) {
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener { response ->
            handleResponse(response)
        }, Response.ErrorListener { error ->
            status.postValue(Companion.Status.ERROR)
            Log.d(TAG, "Response error: $error")
        })

        queue.add(stringRequest)
    }


    override fun handleResponse(response: String) {
        val store = HandleJson.handleJSONStore(response)

        if (store == null) {
            Log.d(TAG, "Unable to handle response")
            return
        }

        result.postValue(store)
        status.postValue(Companion.Status.SUCCESS)
    }

}