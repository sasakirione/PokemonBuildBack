package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.domain.Build
import com.sasakirione.pokebuild.domain.Pokemon

interface IPokemonDataRepository {
    fun getPokemonNameList(input: String): List<String>
    fun getPokemon(id: Int): Pokemon
    fun getGoods(): List<Pair<Int, String>>
    fun getTags(): List<String>

    fun getMoves(): List<Pair<Int, String>>
    fun getPokemonList(): List<Pair<Int, String>>

    fun getBuild(id: Int = 0, authId: String): Build
}