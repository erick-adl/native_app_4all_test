package com.example.task4all.model

import android.arch.lifecycle.MutableLiveData
import android.graphics.drawable.Drawable
import android.os.Handler
import com.example.task4all.api_providers.ImageTask
import com.google.gson.annotations.SerializedName


class Comentario {

    @SerializedName("urlFoto")
    private var urlProfilePhoto: String? = null

    @SerializedName("nome")
    var name: String? = null

    @SerializedName("nota")
    private var evaluation: Int? = null

    @SerializedName("titulo")
    var title: String? = null

    @SerializedName("comentario")
    var content: String? = null


    val profilePicture = MutableLiveData<Drawable>()

    init {
        val handler = Handler()
        handler.postDelayed({ ImageTask(profilePicture).execute(urlProfilePhoto) }, 50)
    }

}