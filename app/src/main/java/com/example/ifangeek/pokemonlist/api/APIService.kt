package com.example.ifangeek.pokemonlist.api

import com.example.ifangeek.pokemonlist.model.PokemonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("pokemon")
    fun obtenerListaPokemon(@Query("limit")limit:Int,@Query("offset")offset:Int) : Call<PokemonResponse>
}