package com.example.task4all.viewmodel

import android.arch.lifecycle.*
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import com.example.task4all.model.Tarefa
import com.example.task4all.api_providers.BaseRequest
import com.example.task4all.api_providers.StoreRequest
import com.example.task4all.view.adapters.CommentsAdapter
import com.google.android.gms.maps.model.LatLng

class TarefaViewModel : ViewModel() {

    private lateinit var request: StoreRequest
    private lateinit var tarefa: Tarefa

    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val comments = MutableLiveData<List<CommentsAdapter.Item>>()
    val position = MutableLiveData<LatLng>()

    lateinit var logo: MutableLiveData<Drawable>
    lateinit var cover: MutableLiveData<Drawable>

    //No need to be and live data, will be shown only when a dialog is performed
    val address: String?
        get() = tarefa.address

    //Data status reflectors
    val dataStatus = MediatorLiveData<BaseRequest.Companion.Status>()
    private val dataStatusObserver = Observer<BaseRequest.Companion.Status> { status ->
        if (status == null) {
            return@Observer
        }

        dataStatus.postValue(status)
    }

    fun init(context: Context, id: String) {
        request = StoreRequest(context, id)
        request.result.observe(context as LifecycleOwner, resultObserver)
    }

    private val resultObserver = Observer<Tarefa> { store ->
        if (store == null) {
            return@Observer
        }

        this.tarefa = store
        handleStoreLoaded()
    }

    private fun handleStoreLoaded() {
        //Spread Tarefa values
        title.postValue(tarefa.title)
        description.postValue(tarefa.content)

        val latLng = LatLng(tarefa.latitude!!, tarefa.longitude!!)
        position.postValue(latLng)

        //Map drawable Live datas
        logo = tarefa.logo
        cover = tarefa.photo

        //After tarefa being found from server, observe the internal status of the loadings (images)
        dataStatus.addSource(tarefa.loadingStatus, dataStatusObserver)

        //Build @CommentsAdapter.Item from @Comments
        val comments = tarefa.comentarios ?: return
        val commentItems = comments.map { CommentsAdapter.Item(it) }
        this.comments.postValue(commentItems)
    }

    //Launch Android Call Application
    fun makeCallForContext(context: Context) {
        val number = tarefa.phone
        val dial = "tel:$number"
        val uri = Uri.parse(dial)
        val intent = Intent(Intent.ACTION_CALL, uri)
        context.startActivity(intent)
    }

}