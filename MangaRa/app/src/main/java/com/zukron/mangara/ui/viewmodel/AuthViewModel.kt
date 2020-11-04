package com.zukron.mangara.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zukron.mangara.model.helper.FavoriteMangaHelper
import com.zukron.mangara.model.helper.HistoryMangaHelper

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 10/3/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class AuthViewModel : ViewModel() {
    private val _isActionSuccess: MutableLiveData<Action> = MutableLiveData()
    private val firebaseDataBase = Firebase.database
    val firebaseAuth = Firebase.auth
    val isActionSuccess: LiveData<Action>
        get() = _isActionSuccess

    fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _isActionSuccess.value = Action(Status.SUCCESS, "Login successful")
                } else {
                    _isActionSuccess.value = Action(Status.FAILED, it.exception?.message ?: "")
                }
            }
    }

    fun signUp(name: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val profilesUpdate = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    firebaseAuth.currentUser?.updateProfile(profilesUpdate)

                    // inisiasi database untuk history dan favorite mencegah salah convert
                    // antara HashMap dengan List, dikarenakan jika index ke 0 di hapus
                    // maka firebase mendeteksi itu menjadi HashMap
                    val refFavorite = firebaseDataBase.getReference("favorite")
                        .child(firebaseAuth.currentUser?.uid!!)
                    val favoriteMangaHelper = FavoriteMangaHelper(indexData = "0")
                    refFavorite.child("0").setValue(favoriteMangaHelper)

                    val refHistory = firebaseDataBase.getReference("history")
                        .child(firebaseAuth.currentUser?.uid!!)
                    val historyMangaHelper = HistoryMangaHelper(indexData = "0")
                    refHistory.child("0").setValue(historyMangaHelper)

                    _isActionSuccess.value = Action(Status.SUCCESS, "Account created")
                } else {
                    _isActionSuccess.value = Action(Status.FAILED, it.exception?.message ?: "")
                }
            }
    }

    enum class Status {
        SUCCESS,
        FAILED
    }

    class Action(val status: Status, val message: String)
}