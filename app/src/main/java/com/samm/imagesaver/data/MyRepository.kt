package com.samm.imagesaver.data

import androidx.lifecycle.LiveData
import com.samm.imagesaver.domain.Place

class MyRepository(private val myDao: MyDao) {

    val readPerson: LiveData<List<Place>> = myDao.readPlace()

    suspend fun insertPlace(place: Place){
        myDao.insertPlace(place)
    }

}