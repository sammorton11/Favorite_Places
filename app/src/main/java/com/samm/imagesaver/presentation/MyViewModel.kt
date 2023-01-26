package com.samm.imagesaver.presentation

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.samm.imagesaver.data.MyDatabase
import com.samm.imagesaver.data.MyRepository
import com.samm.imagesaver.domain.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(application: Application): AndroidViewModel(application) {

    private val dao = MyDatabase.getDatabase(application).myDao()
    private val repository = MyRepository(dao)

    val readPerson: LiveData<List<Place>> = repository.readPerson

    fun insertPlace(place: Place){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPlace(place)
        }
    }

    fun clearAllPlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAllPlaces()
        }
    }

    suspend fun getBitmap(context: Context, imageUri: Uri): Bitmap? {
        val loading = context?.let { ImageLoader(it) }
        val request = context?.let {
            ImageRequest.Builder(it)
                .data(imageUri)
                .build()
        }
        val result = (request?.let { loading!!.execute(it) })
        val bitmap = (result?.drawable as BitmapDrawable).bitmap

        return when (result) {
            is SuccessResult -> bitmap
            is ErrorResult -> {
                throw result.throwable
            }
            else -> null
        }
    }
}
