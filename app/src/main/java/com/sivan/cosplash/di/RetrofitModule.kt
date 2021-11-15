package com.sivan.cosplash.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sivan.cosplash.BuildConfig
import com.sivan.cosplash.network.CoSplashInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideCoSplashInterface(retrofit: Retrofit.Builder): CoSplashInterface {
        val logging = HttpLoggingInterceptor(
            HttpLoggingInterceptor.Logger {
                Timber.tag("OkHttp").d(it)
            }
        )

        return retrofit.client(
            OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor {
                    val requestBuilder = it.request().newBuilder()
                        .addHeader("Accept-Version", "v1")
                        .addHeader("Authorization", BuildConfig.ACCESS_KEY)

                    it.proceed(requestBuilder.build())
                }
                .build()
        )
            .build().create(CoSplashInterface::class.java)
    }
}
