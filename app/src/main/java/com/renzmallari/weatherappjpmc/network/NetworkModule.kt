package com.renzmallari.weatherappjpmc.network


import com.renzmallari.weatherappjpmc.BuildConfig
import com.renzmallari.weatherappjpmc.util.Constants
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
class NetworkModule {

    @Provides
    @Singleton
    fun providesBaseURl() = BuildConfig.URL_PATH

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)

    @Provides
    @Singleton
    fun providesOkHttpClient() = OkHttpClient.Builder()
        .connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(providesHttpLoggingInterceptor())
        .build()

    @Provides
    @Singleton
    fun providesRetrofitService(): Retrofit = Retrofit.Builder()
        .baseUrl(providesBaseURl())
        .addConverterFactory(GsonConverterFactory.create())
        .client(providesOkHttpClient())
        .build()

    @Provides
    @Singleton
    fun providesWeatherApiService(): WeatherApiService = providesRetrofitService().create(WeatherApiService::class.java)

}