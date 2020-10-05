package com.zukron.mangara.network

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/28/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class NetworkState(val message: String) {
    companion object {
        val LOADING = NetworkState("")
        val LOADED = NetworkState("")
        val TIMEOUT = NetworkState("Slow connection")
        val NO_CONNECTION = NetworkState("No internet connection")
        val END_OF_PAGE = NetworkState("All data loaded")
        val ERROR = NetworkState("Something wrong, i can feel it")
    }
}