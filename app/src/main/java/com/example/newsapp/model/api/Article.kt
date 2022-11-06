package com.example.newsapp.model.api

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null, // Needed to save to local db
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
) : Parcelable{
    override fun hashCode(): Int {
        var result = id.hashCode()
        if (url.isNullOrEmpty()){
            result = 31 * result + url.hashCode()
        }

        return result
    }
}