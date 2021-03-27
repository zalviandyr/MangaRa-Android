package com.zukron.mangara.network

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.zukron.mangara.tools.Utilities
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/28/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
object RestApi {
    private const val BASE_URL = "https://mangamint.kaedenoki.net/api/"
    private const val cacheSize = (10 * 1024 * 1024).toLong() // 10 mb
    private var okHttpClient: OkHttpClient? = null

    private val httpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.HEADERS)

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    fun getApiService(context: Context): ApiService {
        val cache = Cache(File(context.cacheDir, "mangara-cache"), cacheSize)

        okHttpClient ?: OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(object : OfflineInterceptor() {
                override fun isInternetAvailable(): Int {
                    return Utilities.getConnectionType(context)
                }

                override fun onInternetUnavailable() {
                    networkState.postValue(NetworkState.NO_CONNECTION)
                }
            })
            .addNetworkInterceptor(OnlineInterceptor())
            .also {
                okHttpClient = it.build()
            }

        return apiService
    }

    private val retrofit: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient!!)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    }

    private val apiService: ApiService by lazy {
        retrofit.build()
            .create(ApiService::class.java)
    }
}