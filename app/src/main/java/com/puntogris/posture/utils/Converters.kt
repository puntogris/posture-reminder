package com.puntogris.posture.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

object Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String): List<Int> {
        val type = object: TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromList(list: List<Int>): String {
        val type = object: TypeToken<List<Int>>() {}.type
        return gson.toJson(list, type)
    }
}