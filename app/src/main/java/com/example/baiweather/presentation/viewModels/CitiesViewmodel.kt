package com.example.baiweather.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baiweather.data.local.dao.CitiesDatabase
import com.example.baiweather.data.local.model.CityEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewmodel @Inject constructor(private val citiesDatabase: CitiesDatabase) :
    ViewModel() {

    private val _searchResult =
        MutableSharedFlow<List<CityEntity>>()
    val searchResult: SharedFlow<List<CityEntity>> =
        _searchResult.asSharedFlow()

    fun searchByName(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchResult.emit(citiesDatabase.getCitiesDao().findByName("$city%"))
        }
    }

    fun searchById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchResult.emit(citiesDatabase.getCitiesDao().loadAllByIds(intArrayOf(id)))
        }
    }

    fun getCities() {
        viewModelScope.launch(Dispatchers.IO) {
            _searchResult.emit(citiesDatabase.getCitiesDao().getFavourites())
        }
    }

    fun setBookmarkCity(isFavourite: Boolean, cityId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            citiesDatabase.getCitiesDao().setBookmark(isFavourite, cityId)
        }
    }
}