package com.example.task4all.helpers

import android.util.Log
import com.example.task4all.model.Lista
import com.example.task4all.model.Tarefa
import com.google.gson.Gson

class HandleJson {

    companion object {
        //TODO: Use Generics here instead of a function for each JSON?
        fun handleJsonResult(response: String): List<String>? {
            val gson = Gson()
            val result = gson.fromJson(response, Lista::class.java)
            val list: List<String>? = result.list

            if (list == null) {
                Log.d(TAG, "No valid list returned from request")
                return null
            }

            return list
        }

        fun handleJSONStore(response: String): Tarefa? {
            val gson = Gson()
            val store = gson.fromJson(response, Tarefa::class.java)

            if (store == null) {
                Log.d(TAG, "No valid store returned from request")
                return null
            }

            return store
        }

        private const val TAG = "HandleJson"
    }
}
