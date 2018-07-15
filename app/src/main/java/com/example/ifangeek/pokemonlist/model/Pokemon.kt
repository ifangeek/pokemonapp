package com.example.ifangeek.pokemonlist.model

import android.util.Log.d

data class Pokemon(
       val name: String,
       val url: String
) {
    val number : Int
        get(){
            val split = url.split("/")
            return split[split.size-2].toInt()
        }
}