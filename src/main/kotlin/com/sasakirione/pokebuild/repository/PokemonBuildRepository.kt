package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.domain.Build
import com.sasakirione.pokebuild.domain.BuildWithoutPokemonList
import com.sasakirione.pokebuild.domain.GrownPokemon
import com.sasakirione.pokebuild.entity.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PokemonBuildRepository : IPokemonBuildRepository {
    override fun getBuild(id: Int, authId: String): Build {
        val buildId: Int
        val build = if (id == 0) {
            val build = PokemonBuilds.innerJoin(Users).select { Users.authId eq authId }
            val isBuildExist = build.count() > 0
            if (isBuildExist) {
                buildId = build.map { it[PokemonBuilds.id] }.single().value
                PokemonBuildMap.innerJoin(GrownPokemons).innerJoin(PokemonBuilds).innerJoin(Goods).innerJoin(Abilities)
                    .innerJoin(Pokemons)
                    .select(PokemonBuilds.id.eq(buildId))
            } else {
                buildId = (PokemonBuilds.insert {
                    it[name] = "構築"
                    it[user] = Users.select { Users.authId eq authId }.single()[Users.id]
                } get PokemonBuilds.id).value
                return Build(buildId , "構築", mutableListOf())
            }
        } else {
            buildId = id
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
        if (pokemonList.isEmpty()) {
            val build2 = PokemonBuilds.select { PokemonBuilds.id eq buildId }
            return Build(
                id = build2.map { it[PokemonBuilds.id] }[0].value,
                name = build2.map { it[PokemonBuilds.name] }[0],
                pokemons = mutableListOf())
        }
        return Build(
            id = build.map { it[PokemonBuildMap.build] }[0].value,
            name = build.map { it[PokemonBuilds.name] }[0],
            pokemons = pokemonList,
        )
    }

    override fun insertPokemon(pokemon: GrownPokemon, buildId: Int, authId: String): Int {
        val exist = PokemonBuilds.innerJoin(Users).select { (Users.authId eq authId) and (PokemonBuilds.id eq buildId) }
            .count() > 0
        if (!exist) {
            throw IllegalArgumentException("Build not found")
        }
        val moves = Moves.select { Moves.name.inList(pokemon.moveList) }.map { it[Moves.id] }.toList()
        val pokemonId = GrownPokemons.insert {
            it[GrownPokemons.pokemon] = pokemon.id
            it[ivH] = pokemon.iv[0]
            it[ivA] = pokemon.iv[1]
            it[ivB] = pokemon.iv[2]
            it[ivC] = pokemon.iv[3]
            it[ivD] = pokemon.iv[4]
            it[ivS] = pokemon.iv[5]
            it[evH] = pokemon.ev[0]
            it[evA] = pokemon.ev[1]
            it[evB] = pokemon.ev[2]
            it[evC] = pokemon.ev[3]
            it[evD] = pokemon.ev[4]
            it[evS] = pokemon.ev[5]
            it[nature] = pokemon.nature
            it[ability] =
                Abilities.select { Abilities.name eq pokemon.ability }.map { row -> row[Abilities.id] }[0].value
            it[move1] = moves[0]
            it[move2] = moves[1]
            it[move3] = moves[2]
            it[move4] = moves[3]
            if (pokemon.good != "選択なし") {
                it[good] = Goods.select { Goods.name eq pokemon.good }.map { row -> row[Goods.id] }[0].value
            } else {
                it[good] = 1
            }
            it[user] = Users.select { Users.authId eq authId }.map { row -> row[Users.id] }[0].value
        } get GrownPokemons.id
        PokemonBuildMap.insert {
            it[build] = buildId
            it[PokemonBuildMap.pokemon] = pokemonId
        }
        return pokemonId.value
    }

    override fun updateGood(goodId: Int, pokemonId: Int, authId: String) {
        checkAuthId(authId, pokemonId)
        GrownPokemons.update({ GrownPokemons.id eq pokemonId }) {
            it[good] = goodId
        }
    }

    private fun checkAuthId(authId: String, pokemonId: Int) {
        val exist =
            GrownPokemons.innerJoin(Users).select { (Users.authId eq authId) and (GrownPokemons.id eq pokemonId) }
                .count() > 0
        if (!exist) {
            throw IllegalArgumentException("Pokemon not found")
        }
    }

    override fun updateEv(ev: List<Int>, pokemonId: Int, authId: String) {
        checkAuthId(authId, pokemonId)
        GrownPokemons.update({ GrownPokemons.id eq pokemonId }) {
            it[evH] = ev[0]
            it[evA] = ev[1]
            it[evB] = ev[2]
            it[evC] = ev[3]
            it[evD] = ev[4]
            it[evS] = ev[5]
        }
    }

    override fun updateAbility(abilityName: String, pokemonId: Int, authId: String) {
        checkAuthId(authId, pokemonId)
        GrownPokemons.update({ GrownPokemons.id eq pokemonId }) {
            it[ability] = Abilities.select { Abilities.name eq abilityName }.map { row -> row[Abilities.id] }[0].value
        }
    }

    override fun updateTag(tagNames: List<String>, pokemonId: Int, authId: String) {
        checkAuthId(authId, pokemonId)
        val tags = PokemonTags.select { PokemonTags.name.inList(tagNames) }.map { it[PokemonTags.id] }.toList()
        PokemonTagMap.deleteWhere { (PokemonTagMap.pokemon eq pokemonId) and (PokemonTagMap.tag.notInList(tags)) }
        tags.filter { tag ->
            PokemonTagMap.select { (PokemonTagMap.pokemon eq pokemonId) and (PokemonTagMap.tag eq tag) }.count() < 1
        }
            .forEach { tag ->
                PokemonTagMap.insert {
                    it[pokemon] = pokemonId
                    it[PokemonTagMap.tag] = tag
                }
            }
    }

    override fun updateNature(natureName: String, pokemonId: Int, authId: String) {
        checkAuthId(authId, pokemonId)
        GrownPokemons.update({ GrownPokemons.id eq pokemonId }) {
            it[nature] = Natures.select { Natures.name eq natureName }.map { row -> row[Natures.id] }[0].value
        }
    }

    override fun updateMoves(moveNames: List<String>, pokemonId: Int, authId: String) {
        checkAuthId(authId, pokemonId)
        val moves = Moves.select { Moves.name.inList(moveNames) }.map { it[Moves.id] }.toList()
        GrownPokemons.update({ GrownPokemons.id eq pokemonId }) {
            it[move1] = moves[0]
            it[move2] = moves[1]
            it[move3] = moves[2]
            it[move4] = moves[3]
        }
    }

    override fun checkUser(authId: String) {
        val isExist = Users.select { Users.authId eq authId }.count() > 0
        if (!isExist) {
            Users.insert {
                it[name] = "ポケモントレーナー"
                it[Users.authId] = authId
                it[profile] = "デフォルトのプロフィールです"
                it[icon] = "default.png"
            }
        }
    }

    override fun deletePokemon(pokemonId: Int, authId: String) {
        checkAuthId(authId, pokemonId)
        PokemonTagMap.deleteWhere { PokemonTagMap.pokemon eq pokemonId }
        PokemonBuildMap.deleteWhere { PokemonBuildMap.pokemon eq pokemonId }
        GrownPokemons.deleteWhere { GrownPokemons.id eq pokemonId }
    }

    override fun getBuildList(authId: String): List<BuildWithoutPokemonList> {
        val userId = Users.select { Users.authId eq authId }.map { row -> row[Users.id] }[0].value
        val query = PokemonBuilds.select { PokemonBuilds.user eq userId }
        return query.map { row ->
            BuildWithoutPokemonList(
                row[PokemonBuilds.id].value,
                row[PokemonBuilds.name],
                row[PokemonBuilds.comment]  ?: "",
            )
        }
    }
}