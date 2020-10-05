package com.zukron.mangara.network

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/29/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
abstract class OfflineInterceptor : Interceptor {
    private val HEADER_CACHE_CONTROL = "Cache-Control"
    private val HEADER_PRAGMA = "Pragma"
    abstract fun isInternetAvailable(): Int
    abstract fun onInternetUnavailable()

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (isInternetAvailable() == 0) {
            onInternetUnavailable()

            val cacheControl = CacheControl.Builder()
                .maxStale(7, TimeUnit.DAYS)
                .build()

            request = request.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .cacheControl(cacheControl)
                .build()
        }
        return chain.proceed(request)
    }
}