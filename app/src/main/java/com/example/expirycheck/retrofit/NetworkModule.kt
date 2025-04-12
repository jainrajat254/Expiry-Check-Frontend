package com.example.expirycheck.retrofit

import android.content.Context
import com.example.expirycheck.barcode.BarcodeScanner
import com.example.expirycheck.repository.PreferencesRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val BASE_URL = "https://de.openfoodfacts.org/api/v2/product/"
    private const val BACKEND_BASE_URL = "https://bbd2-152-59-120-168.ngrok-free.app"

    private const val TIMEOUT = 30L

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .serializeNulls()
            .create()
    }

    @Provides
    @Singleton
    @Named("BarcodeRetrofit")
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @Named("BackendRetrofit")
    fun provideBackendRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @Named("BackendService")
    fun provideBackendService(@Named("BackendRetrofit") backendRetrofit: Retrofit): UserService {
        return backendRetrofit.create(UserService::class.java)
    }


    @Provides
    @Singleton
    @Named("BarcodeService")
    fun provideApiService(@Named("BarcodeRetrofit")retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(@ApplicationContext context: Context): PreferencesRepository {
        return PreferencesRepository(context)
    }

    @Provides
    fun provideBarcodeScanner(@ApplicationContext context: Context): BarcodeScanner {
        return BarcodeScanner(context)
    }
}
