package com.example.newsapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.model.api.Article


@Database(entities = [Article::class], version = 1)
@TypeConverters(Converters::class)

abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao() : ArticleDAO

    companion object{
        @Volatile
        private var INSTANCE : ArticleDatabase? = null

        fun getDatabase(context: Context): ArticleDatabase{

//            If the instance is null, then create the database, and if it is not null, then return it.
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticleDatabase::class.java,
                    "article_db.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}