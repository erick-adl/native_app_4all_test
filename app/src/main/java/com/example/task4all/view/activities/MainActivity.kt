package com.example.task4all.view.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.example.task4all.view.adapters.MainListAdapter
import com.example.task4all.viewmodel.MainActivityViewModel
import com.example.task4all.R
import com.example.task4all.api_providers.BaseRequest

class MainActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: MainListAdapter
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var loadingLayout: View

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen_activity)
        bindLayout()
        initViewModel()
        configureLayout()
    }

    private fun bindLayout() {
        recycler = findViewById(R.id.recycler)
        loadingLayout = findViewById(R.id.loading_layout)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.init(this)
    }

    private fun configureLayout() {
        viewModel.status.observe(this, statusObserver)
    }

    private val statusObserver = Observer<BaseRequest.Companion.Status> { status ->
        if (status == null) {
            return@Observer
        }

        when (status) {
            BaseRequest.Companion.Status.LOADING -> onLoadingStarted()
            BaseRequest.Companion.Status.SUCCESS -> onLoadingFinished()
            BaseRequest.Companion.Status.ERROR -> onLoadingError()
        }
    }

    private val optionsObserver = Observer<List<String>> { list ->
        if (list == null) {
            return@Observer
        }

        handleOptionsValue(list)
        onLoadingFinished()
    }

    private fun onLoadingStarted() {
        loadingLayout.visibility = View.VISIBLE
    }

    private fun onLoadingFinished() {
        loadingLayout.visibility = View.GONE
        viewModel.options.observe(this, optionsObserver)
    }

    //Only bind text error and progress bar here, there's no need to do this before
    private fun onLoadingError() {
        val errorView: TextView = findViewById(R.id.error_view)
        if (errorView == null) {
            Log.d(TAG, "Error view not found")
            return
        }

        errorView.visibility = View.VISIBLE

        val progressBar: ProgressBar = findViewById(R.id.loading_layout_progressBar)

        progressBar.visibility = View.INVISIBLE
    }

    private fun handleOptionsValue(options: List<String>) {
        val items = options.map { MainListAdapter.Item(it) }
        adapter = MainListAdapter(items)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }
}
