package com.dev.quickcart.di

import android.content.Context
import com.dev.quickcart.MyApp
import com.dev.quickcart.R
import com.dev.quickcart.data.repository.NetworkRepository
import com.dev.quickcart.data.repository.NetworkRepositoryImpl
import com.dev.quickcart.navigation.Navigator
import com.dev.quickcart.screens.login.splash_screen.NetworkChecker
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideNetworkRepository(networkRepository: NetworkRepositoryImpl): NetworkRepository {
        return networkRepository
    }

    @Singleton
    @Provides
    fun provideGoogleSignInClient(@ApplicationContext context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }


    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideNetworkChecker(@ApplicationContext context: Context): NetworkChecker {
        return NetworkChecker(context)
    }


}