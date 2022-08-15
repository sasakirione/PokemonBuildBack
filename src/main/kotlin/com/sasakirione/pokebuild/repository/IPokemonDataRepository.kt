package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.domain.Pokemon

interface IPokemonDataRepository {
    fun getPokemonNameList(input: String): List<String>
    fun getPokemon(id: Int): Pokemon
    fun getGoods(): List<Pair<Int, String>>
    fun getTags(): List<String>

    fun getMoves(): List<String>
}