package com.samm.imagesaver.domain

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "my_table")
data class Place(
    val title: String,
    val description: String,
    val image: Bitmap,
    val latitude: Double? = null,
    val longitude: Double? = null
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}