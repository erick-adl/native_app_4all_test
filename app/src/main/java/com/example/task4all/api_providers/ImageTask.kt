package com.example.task4all.api_providers

import android.arch.lifecycle.MutableLiveData
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import java.io.InputStream
import java.lang.Exception
import java.net.URL

class ImageTask(private val response: MutableLiveData<Drawable>) : AsyncTask<String, Void, Boolean>() {

    override fun doInBackground(vararg params: String?): Boolean {
        val url = params[0] ?: return true
        val drawable = findImage(url) ?: return false
        response.postValue(drawable)
        return true
    }

    private fun findImage(urlPath: String): Drawable? {
        return try {
            val url = URL(urlPath)
            val `is` = url.content as InputStream
            Drawable.createFromStream(`is`, "pnaer.png")
        } catch (e: Exception) {
            null
        }
    }

    override fun onPostExecute(result: Boolean?) {
        if (result != null && result == true) {
            return
        }
    }


}