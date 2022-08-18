package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.domain.Build
import com.sasakirione.pokebuild.domain.GrownPokemon
import com.sasakirione.pokebuild.domain.Pokemon
import com.sasakirione.pokebuild.entity.*
import com.sasakirione.pokebuild.entity.GameVersions.entityId
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

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

    override fun getGoods(): List<Pair<Int, String>> =
        Goods.selectAll().map { it[Goods.id].value to it[Goods.name] }

    override fun getTags(): List<String> =
        PokemonTags.selectAll().map { it[PokemonTags.name] }

    override fun getMoves(): List<Pair<Int, String>> =
        Moves.selectAll().map { it[Moves.id].value to it[Moves.name] }

    override fun getPokemonList(): List<Pair<Int, String>> =
        Pokemons.selectAll().map { it[Pokemons.id].value to (it[Pokemons.name] + " " + (it[Pokemons.formName] ?: "")) }

    override fun getBuild(id: Int, authId: String): Build {
        val build = if (id == 0) {
            val buildId = PokemonBuilds.innerJoin(Users).select { Users.authId eq authId }.map { it[PokemonBuilds.id] }.single()
            PokemonBuildMap.innerJoin(GrownPokemons).innerJoin(PokemonBuilds).innerJoin(Goods).innerJoin(Abilities).innerJoin(Pokemons)
                .select(PokemonBuilds.id.eq(buildId))
        } else {
            PokemonBuildMap.innerJoin(GrownPokemons).innerJoin(PokemonBuilds).innerJoin(Goods).innerJoin(Abilities).innerJoin(Pokemons).innerJoin(Users)
                .select((PokemonBuilds.id eq id) and (Users.authId eq authId))
        }
        val pokemonList =  build.map{GrownPokemon(
            id = it[GrownPokemons.pokemon].value,
            name = it[Pokemons.name] + " " + (it[Pokemons.formName] ?: ""),
            personalId = it[GrownPokemons.id].value,
            iv= listOf(it[GrownPokemons.ivH], it[GrownPokemons.ivA], it[GrownPokemons.ivB], it[GrownPokemons.ivC], it[GrownPokemons.ivD], it[GrownPokemons.ivS]),
            ev= listOf(it[GrownPokemons.evH], it[GrownPokemons.evA], it[GrownPokemons.evB], it[GrownPokemons.evC], it[GrownPokemons.evD], it[GrownPokemons.evS]),
            nature= it[GrownPokemons.nature].value,
            ability= it[Abilities.name],
            abilityList= PokemonAbilityMap.innerJoin(Abilities).select { PokemonAbilityMap.pokemon eq it[GrownPokemons.pokemon] }
                .map { row -> row[Abilities.name] },
            bv= listOf(it[Pokemons.baseH], it[Pokemons.baseA], it[Pokemons.baseB], it[Pokemons.baseC], it[Pokemons.baseD], it[Pokemons.baseS]),
            moveList= Moves.select { Moves.id.inList(
                listOf(it[GrownPokemons.move1]!!.value, it[GrownPokemons.move2]!!.value, it[GrownPokemons.move3]!!.value, it[GrownPokemons.move4]!!.value)) }
                .toList().map { row -> row[Moves.name] },
            good= it[Goods.name],
            tag= PokemonTagMap.innerJoin(PokemonTags).select {  PokemonTagMap.pokemon eq it[GrownPokemons.id].value }.map { row -> row[PokemonTags.name] }
        )}
        return Build(
            id = build.map { it[PokemonBuildMap.build] }[0].value,
            name = build.map { it[PokemonBuilds.name] }[0],
            pokemons = pokemonList,
        )
    }

}