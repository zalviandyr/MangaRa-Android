package com.zukron.mangara

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.threetenabp.AndroidThreeTen
import com.zukron.mangara.network.ApiService
import com.zukron.mangara.network.RestApi
import com.zukron.mangara.repository.HomeRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    companion object {
        private const val TAG = "ExampleInstrumentedTest"
    }

    private lateinit var apiService: ApiService

    @Before
    fun init() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        apiService = RestApi.getApiService(appContext)
    }

    @Test
    fun testFlowable() {
        apiService.getPopularManga(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "testFlowable: ${it.data}")
            }, {
                Log.d(TAG, "testFlowable: $it")
            })
        Log.d(TAG, "testFlowable: sate")
    }

//    @Test
//    fun testCall() {
//        apiService.getPopularMangaCall(1).enqueue(object : Callback<PopularMangaResponse> {
//            override fun onResponse(call: Call<PopularMangaResponse>, response: Response<PopularMangaResponse>) {
//                Log.d(TAG, "onResponse: ${response.body()}")
//            }
//
//            override fun onFailure(call: Call<PopularMangaResponse>, t: Throwable) {
//                Log.e(TAG, "onFailure: $t")
//            }
//        })
//    }

    @Test
    fun testFirebaseDatabase() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val firebaseApp = FirebaseApp.initializeApp(appContext)
        val database = FirebaseDatabase.getInstance(firebaseApp!!)
        val ref = database.getReference("favorite")

        ref.setValue("Sate")
    }

    @Test
    fun buildGreeting() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val TAG = "HomeFragment"

        AndroidThreeTen.init(appContext)
        val localTime = LocalTime.parse("23:50", timeFormatter).toNanoOfDay()

        // good morning = 24:00 - 09:59
        val morning1 = LocalTime.parse("24:00", timeFormatter).toNanoOfDay()
        val morning2 = LocalTime.parse("09:59", timeFormatter).toNanoOfDay()

        // good afternoon = 10:00 - 15:59
        val afternoon1 = LocalTime.parse("10:00", timeFormatter).toNanoOfDay()
        val afternoon2 = LocalTime.parse("15:59", timeFormatter).toNanoOfDay()

        // good evening = 16:00 - 23:59
        val evening1 = LocalTime.parse("16:00", timeFormatter).toNanoOfDay()
        val evening2 = LocalTime.parse("23:59", timeFormatter).toNanoOfDay()

        Log.d(TAG, "buildGreeting: $localTime")
        Log.d(TAG, "buildGreeting: $evening1")
        Log.d(TAG, "buildGreeting: $evening2")

        Log.d(TAG, "buildGreetingSAEKADAL: ${localTime in (evening1 + 1) until evening2}")
    }

    @Test
    fun testFavoriteMangaHelper() {
        // login
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val app = FirebaseApp.initializeApp(appContext)
        val auth = FirebaseAuth.getInstance(app!!)
        auth.signInWithEmailAndPassword("reitaray5@gmail.com", "Mean1234")
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "testFavoriteMangaHelper: Masuk")
                    val homeRepository = HomeRepository.getInstance(appContext)
                    homeRepository.getFavoriteManga(auth.currentUser!!)
                }
            }
    }
}