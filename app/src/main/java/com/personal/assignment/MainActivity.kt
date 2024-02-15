package com.personal.assignment

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.personal.assignment.adapter.MainRVAdapter
import com.personal.assignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val context = this@MainActivity
    private lateinit var viewModel: MainViewModel


    private val TAG = "MainActivityLogs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(context, R.layout.activity_main)
        viewModel = ViewModelProvider(context, MainVMFactory(context))[MainViewModel::class.java]
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        viewModel.carsLiveData.observe(context) {
            Log.d(TAG, "onCreate: $it")
            binding.mainRV.layoutManager = LinearLayoutManager(context)
            val mainRVAdapter = MainRVAdapter(context, it)
            binding.mainRV.adapter = mainRVAdapter
            initViews(mainRVAdapter)

        }
    }

    private fun initViews(mainRVAdapter: MainRVAdapter) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mainRVAdapter.filter.filter(newText)
                return true
            }
        })

    }
}