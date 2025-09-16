package com.kcbiswash.course1.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kcbiswash.course1.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://nit3213api.onrender.com/"

    @Provides @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides @Singleton
    fun provideOkHttp(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            // Change to BODY when debugging, BASIC/HEADERS for less noise
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            // Generous timeouts to survive cold start on the server
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(logger)
            .build()
    }

    @Provides @Singleton
    fun provideRetrofit(okHttp: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL) // MUST end with '/'
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides @Singleton
    fun provideApi(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}
