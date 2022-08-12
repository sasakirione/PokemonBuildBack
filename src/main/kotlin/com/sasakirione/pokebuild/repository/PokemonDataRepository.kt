package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.domain.Pokemon
import com.sasakirione.pokebuild.entity.*
import org.jetbrains.exposed.sql.select

class PokemonDataRepository: IPokemonDataRepository {
    override fun getPokemonNameList(input: String): List<String> =
        Pokemons.select { Pokemons.name like "%$input%"}.map { it[Pokemons.name] }

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
}