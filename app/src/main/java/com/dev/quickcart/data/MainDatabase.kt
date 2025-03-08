package com.dev.quickcart.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dev.quickcart.data.model.Product


@Database(
    entities = [Product::class], version = 1, exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {

    abstract fun dataDao(): DataDao


    companion object {

        fun getInstance(context: Context, dbName: String) : MainDatabase {

            val db = Room.databaseBuilder(
                context,
                MainDatabase::class.java,
                dbName
            ).build()
            db.openHelper.setWriteAheadLoggingEnabled(false)
            return db
        }

    }


}