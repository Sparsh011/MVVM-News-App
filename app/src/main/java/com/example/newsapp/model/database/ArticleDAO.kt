package com.example.newsapp.model.database

import androidx.room.*
import com.example.newsapp.model.api.Article
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsertArticle(article: Article) : Long // returns the id that was inserted

    @Query("SELECT * FROM articles")
    fun retrieveSavedArticles() : Flow<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}