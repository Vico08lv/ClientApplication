package com.example.clientapplication.room
import androidx.room.TypeConverter
import com.example.clientapplication.model.response.CommandeResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CommandesResponseTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String): List<CommandeResponse> {
        val listType = object : TypeToken<List<CommandeResponse>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<CommandeResponse>): String {
        return gson.toJson(list)
    }
}

