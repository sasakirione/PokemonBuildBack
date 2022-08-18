package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.domain.Pokemon
import com.sasakirione.pokebuild.entity.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class PokemonDataRepository : IPokemonDataRepository {
    override fun getPokemonNameList(input: String): List<String> =
        Pokemons.select { Pokemons.name like "%$input%" }.map { it[Pokemons.name] }

    override fun getPokemon(id: Int): Pokemon {
        val pokemon = Pokemons.select { Pokemons.id eq id }.single()
        val pokemonType = PokemonTypeMap
            .leftJoin(otherTable = Types)
            .select { PokemonTypeMap.pokemon eq id }
            .map { it[Types.name] }
        val pokemonAbility = PokemonAbilityMap
            .leftJoin(otherTable = Abilities)
            .select { PokemonAbilityMap.pokemon eq id }
            .map { it[Abilities.name] }
        return Pokemon(
            id = pokemon[Pokemons.id].value,
            dexNo = pokemon[Pokemons.dexNo],
            name = pokemon[Pokemons.name],
            formName = pokemon[Pokemons.formName],
            base = listOf(
                pokemon[Pokemons.baseH],
                pokemon[Pokemons.baseA],
                pokemon[Pokemons.baseB],
                pokemon[Pokemons.baseC],
                pokemon[Pokemons.baseD],
                pokemon[Pokemons.baseS]
            ),
            types = pokemonType,
            abilities = pokemonAbility,
        )
    }

    override fun getGoods(): List<Pair<Int, String>> =
        Goods.selectAll().map { it[Goods.id].value to it[Goods.name] }

    override fun getTags(): List<String> =
        PokemonTags.selectAll().map { it[PokemonTags.name] }

    override fun getMoves(): List<Pair<Int, String>> =
        Moves.selectAll().map { it[Moves.id].value to it[Moves.name] }

    override fun getPokemonList(): List<Pair<Int, String>> =
        Pokemons.selectAll().map { it[Pokemons.id].value to (it[Pokemons.name] + " " + (it[Pokemons.formName] ?: "")) }

}