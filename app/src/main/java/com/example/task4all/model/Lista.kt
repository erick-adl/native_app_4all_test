package com.example.task4all.model

import com.google.gson.annotations.SerializedName

class Lista {

    @SerializedName("lista")
    var list: List<String>? = null

    companion object {
        const val ID = "result-id-extra"
    }
}