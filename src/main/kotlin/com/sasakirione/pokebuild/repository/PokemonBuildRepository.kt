package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.domain.Build
import com.sasakirione.pokebuild.domain.GrownPokemon
import com.sasakirione.pokebuild.domain.Pokemon
import com.sasakirione.pokebuild.entity.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class PokemonBuildRepository: IPokemonBuildRepository {
    override fun getBuild(id: Int, authId: String): Build {
        val build = if (id == 0) {
            val build = PokemonBuilds.innerJoin(Users).select { Users.authId eq authId }
            val isBuildExist = build.count() > 0
            val buildId = if (isBuildExist) {
                build.map { it[PokemonBuilds.id] }.single()
            } else {
                PokemonBuilds.insert {
                    it[Users.authId] = authId
                } get PokemonBuilds.id
            }
            PokemonBuildMap.innerJoin(GrownPokemons).innerJoin(PokemonBuilds).innerJoin(Goods).innerJoin(Abilities)
                .innerJoin(Pokemons)
                .select(PokemonBuilds.id.eq(buildId))
        } else {
            PokemonBuildMap.innerJoin(GrownPokemons).innerJoin(PokemonBuilds).innerJoin(Goods).innerJoin(Abilities)
                .innerJoin(Pokemons).innerJoin(Users)
                .select((PokemonBuilds.id eq id) and (Users.authId eq authId))
        }
        val pokemonList = build.map {
            GrownPokemon(
                id = it[GrownPokemons.pokemon].value,
                name = it[Pokemons.name] + " " + (it[Pokemons.formName] ?: ""),
                personalId = it[GrownPokemons.id].value,
                iv = listOf(
                    it[GrownPokemons.ivH],
                    it[GrownPokemons.ivA],
                    it[GrownPokemons.ivB],
                    it[GrownPokemons.ivC],
                    it[GrownPokemons.ivD],
                    it[GrownPokemons.ivS]
                ),
                ev = listOf(
                    it[GrownPokemons.evH],
                    it[GrownPokemons.evA],
                    it[GrownPokemons.evB],
                    it[GrownPokemons.evC],
                    it[GrownPokemons.evD],
                    it[GrownPokemons.evS]
                ),
                nature = it[GrownPokemons.nature].value,
                ability = it[Abilities.name],
                abilityList = PokemonAbilityMap.innerJoin(Abilities)
                    .select { PokemonAbilityMap.pokemon eq it[GrownPokemons.pokemon] }
                    .map { row -> row[Abilities.name] },
                bv = listOf(
                    it[Pokemons.baseH],
                    it[Pokemons.baseA],
                    it[Pokemons.baseB],
                    it[Pokemons.baseC],
                    it[Pokemons.baseD],
                    it[Pokemons.baseS]
                ),
                moveList = Moves.select {
                    Moves.id.inList(
                        listOf(
                            it[GrownPokemons.move1]!!.value,
                            it[GrownPokemons.move2]!!.value,
                            it[GrownPokemons.move3]!!.value,
                            it[GrownPokemons.move4]!!.value
                        )
                    )
                }
                    .toList().map { row -> row[Moves.name] },
                good = it[Goods.name],
                tag = PokemonTagMap.innerJoin(PokemonTags)
                    .select { PokemonTagMap.pokemon eq it[GrownPokemons.id].value }.map { row -> row[PokemonTags.name] }
            )
        }
        return Build(
            id = build.map { it[PokemonBuildMap.build] }[0].value,
            name = build.map { it[PokemonBuilds.name] }[0],
            pokemons = pokemonList,
        )
    }

    override fun insertPokemon(pokemon: Pokemon, buildId: Int, authId: String) {
        TODO("Not yet implemented")
    }
}