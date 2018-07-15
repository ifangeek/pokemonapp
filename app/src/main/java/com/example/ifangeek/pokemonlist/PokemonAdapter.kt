package com.example.ifangeek.pokemonlist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.ifangeek.pokemonlist.model.Pokemon
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_pokemon.view.*

class PokemonAdapter(private val context : Context) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    val items: ArrayList<Pokemon>? = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p: Pokemon = items!![position]
        holder?.itemView.tv_name_pokemon?.text = p.name.toString()
        Picasso.get().load("http://pokeapi.co/media/sprites/pokemon/"+p.number+".png").into(holder?.itemView.iv_pokemon)
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    fun adicionarListaPokemon(listaPokemon: ArrayList<Pokemon>) {
        items!!.addAll(listaPokemon)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivPokemon: ImageView? = itemView.findViewById(R.id.iv_pokemon)
        private val tvPokemon : TextView? = itemView.findViewById(R.id.tv_name_pokemon)


    }
}
