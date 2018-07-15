package com.example.ifangeek.pokemonlist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.Log.i
import com.example.ifangeek.pokemonlist.api.APIService
import com.example.ifangeek.pokemonlist.model.Pokemon
import com.example.ifangeek.pokemonlist.model.PokemonResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var retrofit : Retrofit
    val TAG : String = "POKEDEX"

    lateinit var rvPokemon : RecyclerView
    var listapokemonAdapter : PokemonAdapter? = null

    var offset: Int = 0

    var aptoParaCargar : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvPokemon = findViewById(R.id.rv_pokemons)
        listapokemonAdapter = PokemonAdapter(this)
        rvPokemon.adapter = listapokemonAdapter
        rvPokemon.setHasFixedSize(true)
        var layout  = GridLayoutManager(this,3)
        rvPokemon.layoutManager = layout
        rvPokemon.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy> 0){
                    val visibleItemCount = layout.childCount
                    val totalItemCount = layout.itemCount
                    val pastVisibleItems = layout.findFirstVisibleItemPosition()

                    if(aptoParaCargar){
                        if((visibleItemCount + pastVisibleItems) >= totalItemCount){
                            i(TAG,"Llegamos al final")

                            aptoParaCargar = false
                            obtenerDatos(offset)
                        }
                    }
                }
            }
        })

        val httpClientBuilder = builder()
        val httpClient = httpClientBuilder.build()

        retrofit = Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()

        aptoParaCargar = true
        obtenerDatos()
    }

    private fun builder(): OkHttpClient.Builder {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)

        val loggingInterceptor = HttpLoggingInterceptor { l -> Log.d("HTTP", l) }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        return httpClientBuilder
    }

    private fun obtenerDatos(offset: Int = 0) {

        var service: APIService = retrofit!!.create(APIService::class.java)
        var pokemonResponseCall = service.obtenerListaPokemon(20,offset)

        pokemonResponseCall.enqueue(object : Callback<PokemonResponse?> {
            override fun onFailure(call: Call<PokemonResponse?>?, t: Throwable?) {
               Log.e(TAG,"onFailure : " + t!!.message.toString())
            }

            override fun onResponse(call: Call<PokemonResponse?>?, response: Response<PokemonResponse?>?) {
                if (response!!.isSuccessful) {

                    //pomemonresponse tiene un array de respuesta
                    val pokemonResponse: PokemonResponse = response.body()!!
                    // obtengo un array del objeto pokemon que tiene 2 atributos
                    var listaPokemon : ArrayList<Pokemon> = pokemonResponse.results

//                    var i : Int? = 0
//                    for (pokemon in i!!..listaPokemon.size){
//
//                        var p : Pokemon = listaPokemon[pokemon]
//                        Log.i(TAG,"Pokemon : "+p.name.toString())
//
//                    }

                    this@MainActivity.offset += listaPokemon.size
                    listapokemonAdapter!!.adicionarListaPokemon(listaPokemon)
                    aptoParaCargar = true

                }
                else{
                    Log.e(TAG,"on Response" + response.errorBody())
                }

            }
        })
    }
}
