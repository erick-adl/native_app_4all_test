package com.example.task4all.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.example.task4all.R
import com.example.task4all.model.Lista
import com.example.task4all.api_providers.BaseRequest
import com.example.task4all.view.adapters.CommentsAdapter
import com.example.task4all.helpers.ImageHelper
import com.example.task4all.viewmodel.TarefaViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions

class TarefaActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "TarefaActivity"
    private lateinit var storeId: String

    //Views
    private lateinit var coverPhoto: ImageView
    private lateinit var logoPhoto: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var commentsRecycler: RecyclerView
    private lateinit var loadingLayout: View
    private lateinit var parentScrollView: ScrollView
    private lateinit var mapView: MapView
    private lateinit var googleMaps: GoogleMap
    private lateinit var address: TextView

    //Options
    private lateinit var phoneCall: View
    private lateinit var services: View
    private lateinit var location: View
    private lateinit var comments: View

    private lateinit var mapViewBundle: Bundle

    private lateinit var commentsAdapter: CommentsAdapter

    private lateinit var viewModel: TarefaViewModel

    private val MAKE_CALL_PERMISSION_REQUEST_CODE = 2
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tarefa_activity)
        handleBundle()
        bindLayout()
        bindListeners()
        initViewModel()
        bindMap(savedInstanceState)
    }

    private fun bindMap(savedInstanceState: Bundle?) {
        mapViewBundle = if (savedInstanceState == null) {
            Bundle()
        } else {
            savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {
        googleMaps = map
        googleMaps.setMinZoomPreference(15.0F)
    }

    //Prevents invalid info
    private fun handleBundle() {
        if (intent == null) {
            handleError("Invalid Bundle, finishing activity")
            return
        }

        val id = intent.getStringExtra(Lista.ID)

        if (id == null) {
            handleError("No ID passed, finishing activity")
            return
        }

        storeId = id
    }

    private fun bindLayout() {
        parentScrollView = findViewById(R.id.scrollView)
        coverPhoto = findViewById(R.id.cover_photo)
        logoPhoto = findViewById(R.id.logo_photo)
        title = findViewById(R.id.title)
        description = findViewById(R.id.description)
        mapView = findViewById(R.id.map_view)

        commentsRecycler = findViewById(R.id.comments_recycler)
        loadingLayout = findViewById(R.id.loading_layout)

        phoneCall = findViewById(R.id.call)
        services = findViewById(R.id.services)
        location = findViewById(R.id.address)
        comments = findViewById(R.id.comments)
        address = findViewById(R.id.address_title)
    }

    private fun bindListeners() {
        phoneCall.setOnClickListener { call() }
        services.setOnClickListener(onServicesClicked)
        location.setOnClickListener(onLocationClicked)
        comments.setOnClickListener(onCommentsClicked)
    }

    private val onServicesClicked = View.OnClickListener {
        val intent = Intent(it.context, ServicoActivity::class.java)
        startActivity(intent)
    }

    private val onLocationClicked = View.OnClickListener {
        val dialog = AlertDialog.Builder(it.context)
        dialog.setMessage(viewModel.address)
        dialog.show()
    }

    private val onCommentsClicked = View.OnClickListener {
        parentScrollView.smoothScrollTo(0, commentsRecycler.top)
    }

    private fun bindData() {
        viewModel.title.observe(this, Observer { titleContent ->
            if (titleContent == null) {
                return@Observer
            }

            title.text = titleContent
        })

        viewModel.description.observe(this, Observer { descriptionContent ->
            if (descriptionContent == null) {
                return@Observer
            }

            description.text = descriptionContent
        })

        viewModel.comments.observe(this, Observer { commentsItems ->
            if (commentsItems == null) {
                return@Observer
            }

            commentsAdapter = CommentsAdapter(commentsItems)
            commentsRecycler.layoutManager = LinearLayoutManager(this@TarefaActivity)
            commentsRecycler.adapter = commentsAdapter
        })

        viewModel.cover.observe(this, Observer { drawable ->
            if (drawable == null) {
                return@Observer
            }
            coverPhoto.setImageDrawable(drawable)
        })

        viewModel.logo.observe(this, Observer { drawable ->
            if (drawable == null) {
                return@Observer
            }

            val roundedBitmap = ImageHelper.getRoundedDrawable(drawable)
            logoPhoto.setImageBitmap(roundedBitmap)

        })

        viewModel.position.observe(this, Observer { position ->
            if (position == null) {
                return@Observer
            }
            googleMaps.addMarker(MarkerOptions().position(position))
            googleMaps.moveCamera(CameraUpdateFactory.newLatLng(position))
        })

        address.text = viewModel.address

    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(TarefaViewModel::class.java)
        viewModel.init(this, storeId)
        viewModel.dataStatus.observe(this, statusObserver)
    }

    private val statusObserver = Observer<BaseRequest.Companion.Status> { status ->
        if (status == null) {
            return@Observer
        }

        when (status) {
            BaseRequest.Companion.Status.SUCCESS -> onLoadingFinished()
            BaseRequest.Companion.Status.ERROR -> onLoadingError()
            BaseRequest.Companion.Status.LOADING -> onLoadingStarted()
        }
    }

    private fun onLoadingStarted() {
        loadingLayout.visibility = View.VISIBLE
    }

    private fun onLoadingFinished() {
        loadingLayout.visibility = View.GONE
        bindData()
    }

    private fun onLoadingError() {
        Toast.makeText(this, R.string.request_error, Toast.LENGTH_LONG).show()
        finish()
    }

    private fun handleError(errorMessage: String) {
        Log.d(TAG, errorMessage)
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        finishActivity(Activity.RESULT_CANCELED)
    }

    private fun call() {
        //Before trying to call, we need to ensures that we have permission.
        //If not, request it
        val permissionGranted = checkPermission(Intent.ACTION_CALL)
        if (!permissionGranted) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                MAKE_CALL_PERMISSION_REQUEST_CODE
            )
            return
        }

        viewModel.makeCallForContext(this)
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MAKE_CALL_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.makeCallForContext(this)
                }
                return
            }
        }
    }

    //MapView requires special care with lifecycle

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}