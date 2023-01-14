package com.samm.imagesaver.presentation

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.samm.imagesaver.data.MyDatabase
import com.samm.imagesaver.data.MyRepository
import com.samm.imagesaver.domain.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(application: Application): AndroidViewModel(application) {

    private val dao = MyDatabase.getDatabase(application).myDao()
    private val repository = MyRepository(dao)

    val selectedPlace: MutableLiveData<Place> = MutableLiveData()

    val readPerson: LiveData<List<Place>> = repository.readPerson

    fun insertPlace(place: Place){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPlace(place)
        }
    }
}