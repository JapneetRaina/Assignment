package com.personal.assignment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.personal.assignment.model.Car

class MainViewModel(val context : Context) : ViewModel() {
    val carsLiveData = MutableLiveData<List<Car>>()
    private val _carList = MutableLiveData<List<Car>>()
    val carList: LiveData<List<Car>> = _carList

    init {
        parseJson()
    }

    private fun parseJson() {
        val inputStream = context.resources.openRawResource(R.raw.asset_file)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val gson = Gson()
        val cars: List<Car> = gson.fromJson(jsonString, object : TypeToken<List<Car>>() {}.type)
        carsLiveData.value = cars
    }
}