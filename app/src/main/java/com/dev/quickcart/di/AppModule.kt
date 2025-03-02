package com.dev.quickcart.di

import com.dev.quickcart.MyApp
import com.dev.quickcart.navigation.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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

}