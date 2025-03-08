package com.dev.quickcart.di

import android.content.Context
import com.dev.quickcart.MyApp
import com.dev.quickcart.data.DataDao
import com.dev.quickcart.data.MainDatabase
import com.dev.quickcart.data.repository.DataRepository
import com.dev.quickcart.data.repository.DataRepositoryImpl
import com.dev.quickcart.navigation.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApp(): MyApp {
        return MyApp()
    }

    @Singleton
    @Provides
    fun provideNavigator(): Navigator {
        return Navigator()
    }


    @Singleton
    @Provides
    fun provideDataDao( @ApplicationContext context: Context): DataDao {
        return MainDatabase.getInstance(context, "local.db").dataDao()
    }

    @Singleton
    @Provides
    fun provideDataRepository(dataRepository: DataRepositoryImpl): DataRepository {
        return dataRepository
    }

}