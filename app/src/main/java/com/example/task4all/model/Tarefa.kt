package com.example.task4all.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.graphics.drawable.Drawable
import android.os.Handler
import com.example.task4all.api_providers.BaseRequest
import com.example.task4all.api_providers.ImageTask
import com.google.gson.annotations.SerializedName

class Tarefa {

    @SerializedName("cidade")
    var city: String? = null

    @SerializedName("bairro")
    var district: String? = null

    @SerializedName("urlFoto")
    private var urlPhoto: String? = null

    @SerializedName("urlLogo")
    private var urlLogo: String? = null

    @SerializedName("titulo")
    var title: String? = null

    @SerializedName("telefone")
    var phone: String? = null

    @SerializedName("texto")
    var content: String? = null

    @SerializedName("endereco")
    var address: String? = null

    @SerializedName("latitude")
    var latitude: Double? = null

    @SerializedName("longitude")
    var longitude: Double? = null

    @SerializedName("comentarios")
    var comentarios: List<Comentario>? = null

    //LiveData with Drawables
    val photo = MutableLiveData<Drawable>()
    val logo = MutableLiveData<Drawable>()

    private val imagesObserver = Observer<Drawable> { drawable ->
        if (drawable == null) {
            return@Observer
        }

        verifyImagesAreLoaded()
    }

    //Images take longer to load, then we need to reflect the current status of these loadings.
    //@loadingStatus is set to true when, and only when:
    //1 - @photo is loaded
    //2 - @logo is loaded
    val loadingStatus = MutableLiveData<BaseRequest.Companion.Status>()

    init {
        val handler = Handler()

        handler.postDelayed({
            ImageTask(photo).execute(urlPhoto)
        }, 50)

        handler.postDelayed({
            ImageTask(logo).execute(urlLogo)
        }, 50)

        loadingStatus.value = BaseRequest.Companion.Status.LOADING
        photo.observeForever(imagesObserver)
        logo.observeForever(imagesObserver)
    }

    //If both @photo and @logo are loaded, the store is ready.
    private fun verifyImagesAreLoaded() {
        if (photo.value == null || logo.value == null) {
            return
        }
        loadingStatus.postValue(BaseRequest.Companion.Status.SUCCESS)
    }
}