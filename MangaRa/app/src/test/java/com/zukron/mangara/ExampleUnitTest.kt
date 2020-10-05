package com.zukron.mangara

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.threetenabp.AndroidThreeTen
import com.zukron.mangara.network.RestApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*
import org.threeten.bp.LocalTime
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testingRequest() {
        fun fetchData(starPosition: Int, loadCount: Int, load: (Int, Int) -> Unit) {
            load(9, 8)
        }

        fetchData(0, 3) { r, p ->
            println("$r  $p")
        }

        fun hello(name: String, transformer: (String) -> Unit) {
            val nameTransform = transformer(name)
            println(nameTransform)
        }

        hello("Alvin") {
            println(it)
        }
    }

    enum class Status {
        SUCCESS,
        FAILED
    }

    class Action(val status: Status, val message: String)

    @Test
    fun testEnumClass() {
        val success = Action(Status.SUCCESS, "Success")

        if (success.status == Status.SUCCESS) {

        }
    }
}