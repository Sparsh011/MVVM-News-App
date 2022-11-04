package com.example.newsapp.model.database

import androidx.room.TypeConverter
import com.example.newsapp.model.api.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source) : String{
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source{
        return Source(name, name)
    }
}