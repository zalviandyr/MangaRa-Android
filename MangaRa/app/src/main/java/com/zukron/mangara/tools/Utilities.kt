package com.zukron.mangara.tools

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.jakewharton.threetenabp.AndroidThreeTen
import com.zukron.mangara.R
import com.zukron.mangara.model.helper.MangaHelper
import kotlinx.android.synthetic.main.item_popular_manga.view.*
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/28/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
object Utilities {
    fun getConnectionType(context: Context): Int {
        // Returns connection type. 0: none; 1: mobile data; 2: wifi
        var result = 0
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        networkCapabilities?.run {
            if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                result = 2
            } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                result = 1
            }
        }

        return result
    }

    fun backgroundType(type: String, context: Context): Drawable? {
        return when (type) {
            MangaHelper.Type.manga ->
                ContextCompat.getDrawable(context, R.drawable.bg_round_green)
            MangaHelper.Type.manhua ->
                ContextCompat.getDrawable(context, R.drawable.bg_round_red)
            MangaHelper.Type.manhwa ->
                ContextCompat.getDrawable(context, R.drawable.bg_round_blue)
            else -> null
        }
    }

    fun circularProgressDrawable(context: Context): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.setColorSchemeColors(Color.WHITE)
        circularProgressDrawable.start()

        return circularProgressDrawable
    }

    fun greeting(context: Context): String {
        var greeting = ""
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        AndroidThreeTen.init(context)
        val localTime = LocalTime.now().toNanoOfDay()

        // good morning = 24:00 - 09:59
        val morning1 = LocalTime.parse("24:00", timeFormatter).toNanoOfDay()
        val morning2 = LocalTime.parse("09:59", timeFormatter).toNanoOfDay()

        // good afternoon = 10:00 - 15:59
        val afternoon1 = LocalTime.parse("10:00", timeFormatter).toNanoOfDay()
        val afternoon2 = LocalTime.parse("15:59", timeFormatter).toNanoOfDay()

        // good evening = 16:00 - 23:59
        val evening1 = LocalTime.parse("16:00", timeFormatter).toNanoOfDay()
        val evening2 = LocalTime.parse("23:59", timeFormatter).toNanoOfDay()

        if (localTime in (morning1 + 1) until morning2) {
            greeting = context.getString(R.string.good_morning)
        }

        if (localTime in (afternoon1 + 1) until afternoon2) {
            greeting = context.getString(R.string.good_afternoon)
        }

        if (localTime in (evening1 + 1) until evening2) {
            greeting = context.getString(R.string.good_evening)
        }

        return greeting
    }
}