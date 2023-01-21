package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.domain.Pokemon
import com.sasakirione.pokebuild.entity.PokemonMoveMap
import com.sasakirione.pokebuild.entity.PokemonTypeMap
import com.sasakirione.pokebuild.entity.Pokemons
import com.sasakirione.pokebuild.entity.Types
import com.sasakirione.pokebuild.plugins.MasterCache
import org.jetbrains.exposed.sql.select

class PokemonDataRepository : IPokemonDataRepository {

    override fun getPokemon(id: Int): Pokemon {
        val pokemon = Pokemons.select { Pokemons.id eq id }.single()
        val pokemonType = PokemonTypeMap
            .leftJoin(otherTable = Types)
            .select { PokemonTypeMap.pokemon eq id }
            .map { it[Types.name] }
        val pokemonAbility = MasterCache.getPokemonAbilities(id)
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

    override fun getGoods(): List<Triple<Int, String, String>> = MasterCache.goods

    override fun getTags(): List<String> = MasterCache.tags.map { it.second }

    override fun getMoves(): List<Pair<Int, String>> = MasterCache.moves

    override fun getPokemonList(): List<Pair<Int, String>> = MasterCache.simplePokemons
    override fun getPokemonMove(pokemonId: Int): List<Int> {
        val moveList =
            PokemonMoveMap.select { PokemonMoveMap.pokemon eq pokemonId }.map { it[PokemonMoveMap.move].value }

        return moveList.ifEmpty {
            listOf(0)
        }
    }

}