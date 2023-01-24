package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.domain.Build
import com.sasakirione.pokebuild.domain.BuildWithoutPokemonList
import com.sasakirione.pokebuild.domain.GrownPokemon
import com.sasakirione.pokebuild.entity.*
import com.sasakirione.pokebuild.entity.GrownPokemons.good
import com.sasakirione.pokebuild.entity.TerastalMap.pokemonId
import com.sasakirione.pokebuild.plugins.MasterCache
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PokemonBuildRepository : IPokemonBuildRepository {

    override fun getFirstBuildId(authId: String): Int {
        val query = PokemonBuilds.innerJoin(Users).select { Users.authId eq authId }
        val isExist = query.count() > 0
        return if (isExist) {
            query.first()[PokemonBuilds.id].value
        } else {
            (PokemonBuilds.insert {
                it[name] = "構築"
                it[user] = Users.select { Users.authId eq authId }.single()[Users.id]
            } get PokemonBuilds.id).value
        }
    }

    override fun updateAbility(abilityId: Int, pokemonId: Int, authId: String) {
        checkGrownPokemon(authId, pokemonId)
        GrownPokemons.update({ GrownPokemons.id eq pokemonId }) { it[ability] = abilityId }
    }


    override fun updateNature(natureId: Int, pokemonId: Int, authId: String) {
        checkGrownPokemon(authId, pokemonId)
        GrownPokemons.update({ GrownPokemons.id eq pokemonId }) { it[nature] = natureId }
    }

    override fun updateTag(tagId: List<Int>, pokemonId: Int, authId: String) {
        checkGrownPokemon(authId, pokemonId)
        PokemonTagMap.deleteWhere { pokemon eq pokemonId }
        PokemonTagMap.batchInsert(tagId) { id ->
            this[PokemonTagMap.pokemon] = pokemonId
            this[PokemonTagMap.tag] = id
        }
    }

    override fun updateMove(moveIds: List<Int>, pokemonId: Int, authId: String) {
        checkGrownPokemon(authId, pokemonId)
        GrownPokemons.update({ GrownPokemons.id eq pokemonId }) {
            it[move1] = moveIds[0]
            it[move2] = moveIds[1]
            it[move3] = moveIds[2]
            it[move4] = moveIds[3]
        }
    }

    override fun updateGoodByValue(goodName: String, pokemonId: Int, authId: String) {
        checkGrownPokemon(authId, pokemonId)
        GrownPokemons.innerJoin(Goods).update({ GrownPokemons.id eq pokemonId }) {
            it[good] = MasterCache.getGoodId(goodName)
        }
    }

    override fun getBuild(id: Int, authId: String): Build {
        checkUserBuild(id, authId)
        return getBuild(id)
    }

    private fun getBuild(id: Int): Build {
        val build =
            PokemonBuildMap.innerJoin(GrownPokemons).innerJoin(PokemonBuilds).innerJoin(Goods).innerJoin(Abilities)
                .innerJoin(Pokemons).select((PokemonBuildMap.build eq id))
        val pokemonList = build.map { convertGrownPokemon(it) }
        val build2 = PokemonBuilds.select { PokemonBuilds.id eq id }.single()
        if (pokemonList.isEmpty()) {
            return Build(
                id = build2[PokemonBuilds.id].value,
                name = build2[PokemonBuilds.name],
                pokemons = mutableListOf()
            )
        }
        return Build(
            id = build2[PokemonBuilds.id].value,
            name = build2[PokemonBuilds.name],
            pokemons = pokemonList,
        )
    }

    override fun getGrownPokemon(pokemonId: Int, authId: String): GrownPokemon {
        checkGrownPokemon(authId, pokemonId)
        val pokemon = GrownPokemons.innerJoin(Abilities).innerJoin(Pokemons).innerJoin(Goods)
            .select { GrownPokemons.id eq pokemonId }
        return convertGrownPokemon(pokemon.first())
    }

    override fun getGrownPokemonList(authId: String): List<GrownPokemon> {
        checkUser(authId)
        val userId = getUserIdFromAuthId(authId)
        val pokemonList = GrownPokemons.innerJoin(Abilities).innerJoin(Pokemons).innerJoin(Goods)
            .select { GrownPokemons.user eq userId }
        return pokemonList.map { convertGrownPokemon(it) }
    }

    override fun insertGrownPokemon(pokemon: GrownPokemon, authId: String): Int {
        checkUser(authId)
        val moves = MasterCache.getMoveIdList(pokemon.moveList)
        return getIdFromInsertGrownPokemon(pokemon, moves, authId).value
    }

    override fun deletePokemonFromBuild(buildId: Int, pokemonId: Int, authId: String) {
        checkGrownPokemon(authId, pokemonId)
        PokemonBuildMap.deleteWhere { (build eq buildId) and (pokemon eq pokemonId) }
    }

    private fun convertGrownPokemon(it: ResultRow) = GrownPokemon(
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
        abilityList = MasterCache.getPokemonAbilities(it[Pokemons.id].value),
        bv = listOf(
            it[Pokemons.baseH],
            it[Pokemons.baseA],
            it[Pokemons.baseB],
            it[Pokemons.baseC],
            it[Pokemons.baseD],
            it[Pokemons.baseS]
        ),
        moveList = MasterCache.getMoveNameList(
            listOf(
                it[GrownPokemons.move1]!!.value,
                it[GrownPokemons.move2]!!.value,
                it[GrownPokemons.move3]!!.value,
                it[GrownPokemons.move4]!!.value
            )
        ),
        good = it[Goods.name],
        tag = PokemonTagMap.innerJoin(PokemonTags)
            .select { PokemonTagMap.pokemon eq it[GrownPokemons.id].value }.map { row -> row[PokemonTags.name] },
        nickname = it[GrownPokemons.comment] ?: "",
        terastal = TerastalMap.innerJoin(Types).select { pokemonId eq it[GrownPokemons.id].value }
            .map { row -> row[Types.name] }.firstOrNull(),
    )

    override fun insertPokemon(pokemon: GrownPokemon, buildId: Int, authId: String): Int {
        val exist = PokemonBuilds.innerJoin(Users).select { (Users.authId eq authId) and (PokemonBuilds.id eq buildId) }
            .count() > 0
        if (!exist) {
            throw IllegalArgumentException("Build not found")
        }
        val moveListWithoutDefault = pokemon.moveList.filter { it != "選択なし" }
        val moves = MasterCache.getMoveIdList(moveListWithoutDefault)
        val pokemonId = getIdFromInsertGrownPokemon(pokemon, moves, authId)
        PokemonBuildMap.insert {
            it[build] = buildId
            it[PokemonBuildMap.pokemon] = pokemonId
        }
        if (pokemon.terastal != null && pokemon.terastal != "選択なし") {
            TerastalMap.insert {
                it[TerastalMap.pokemonId] = pokemonId
                it[type] = MasterCache.getTypeId(pokemon.terastal)
            }
        }
        return pokemonId.value
    }

    private fun getIdFromInsertGrownPokemon(
        pokemon: GrownPokemon,
        moves: List<Int>,
        authId: String
    ) = GrownPokemons.insert {
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
        it[ability] = MasterCache.getAbilityId(pokemon.ability)
        it[move1] = moves.getOrNull(0)
        it[move2] = moves.getOrNull(1)
        it[move3] = moves.getOrNull(2)
        it[move4] = moves.getOrNull(3)
        if (pokemon.good != "選択なし") {
            it[good] = MasterCache.getGoodId(pokemon.good)
        } else {
            it[good] = 1
        }
        it[user] = getUserIdFromAuthId(authId)
    } get GrownPokemons.id

    override fun updateGood(goodId: Int, pokemonId: Int, authId: String) {
        checkGrownPokemon(authId, pokemonId)
        GrownPokemons.update({ GrownPokemons.id eq pokemonId }) {
            it[good] = goodId
        }
    }

    private fun checkGrownPokemon(authId: String, pokemonId: Int) {
        val exist =
            GrownPokemons.innerJoin(Users).select { (Users.authId eq authId) and (GrownPokemons.id eq pokemonId) }
                .count() > 0
        if (!exist) {
            throw IllegalArgumentException("指定の育成済みポケモンが存在しません！")
        }
    }

    override fun updateEv(ev: List<Int>, pokemonId: Int, authId: String) {
        checkGrownPokemon(authId, pokemonId)
        GrownPokemons.update({ GrownPokemons.id eq pokemonId }) {
            it[evH] = ev[0]
            it[evA] = ev[1]
            it[evB] = ev[2]
            it[evC] = ev[3]
            it[evD] = ev[4]
            it[evS] = ev[5]
        }
    }

    override fun updateAbilityByValue(abilityName: String, pokemonId: Int, authId: String) {
        checkGrownPokemon(authId, pokemonId)
        GrownPokemons.update({ GrownPokemons.id eq pokemonId }) {
            it[ability] = MasterCache.getAbilityId(abilityName)
        }
    }

    override fun updateTagByValue(tagNames: List<String>, pokemonId: Int, authId: String) {
        checkGrownPokemon(authId, pokemonId)
        val tags = PokemonTags.select { PokemonTags.name.inList(tagNames) }.map { it[PokemonTags.id] }.toList()
        PokemonTagMap.deleteWhere { pokemon eq pokemonId }
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

    override fun updateNatureByValue(natureName: String, pokemonId: Int, authId: String) {
        checkGrownPokemon(authId, pokemonId)
        GrownPokemons.update({ GrownPokemons.id eq pokemonId }) {
            it[nature] = MasterCache.getNatureId(natureName)
        }
    }

    override fun updateMovesByValue(moveNames: List<String>, pokemonId: Int, authId: String) {
        checkGrownPokemon(authId, pokemonId)
        val moves = MasterCache.getMoveIdList(moveNames)
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
        checkGrownPokemon(authId, pokemonId)
        PokemonTagMap.deleteWhere { pokemon eq pokemonId }
        PokemonBuildMap.deleteWhere { pokemon eq pokemonId }
        GrownPokemons.deleteWhere { GrownPokemons.id eq pokemonId }
    }

    override fun getBuildList(authId: String): List<BuildWithoutPokemonList> {
        val userId = getUserIdFromAuthId(authId)
        val query = PokemonBuilds.select { PokemonBuilds.user eq userId }
        if (query.count() < 1) {
            PokemonBuilds.insert {
                it[user] = userId
                it[name] = "デフォルトのビルド"
            }
        }
        return query.map { row ->
            BuildWithoutPokemonList(
                row[PokemonBuilds.id].value,
                row[PokemonBuilds.name],
                row[PokemonBuilds.comment] ?: "",
            )
        }
    }

    override fun createBuild(build: BuildWithoutPokemonList, authId: String): Int {
        checkUser(authId)
        val userId = getUserIdFromAuthId(authId)
        return (PokemonBuilds.insert {
            it[name] = build.name
            it[comment] = build.comment
            it[user] = userId
        } get PokemonBuilds.id).value
    }

    override fun updateBuild(id: Int, build: BuildWithoutPokemonList, authId: String) {
        checkUserBuild(id, authId)
        val userId = getUserIdFromAuthId(authId)
        PokemonBuilds.update({ (PokemonBuilds.id eq id) and (PokemonBuilds.user eq userId) }) {
            it[name] = build.name
            it[comment] = build.comment
        }
    }

    override fun deleteBuild(id: Int, authId: String) {
        checkUserBuild(id, authId)
        val userId = getUserIdFromAuthId(authId)
        PokemonBuildMap.deleteWhere { (build eq id) }
        PokemonBuilds.deleteWhere { (PokemonBuilds.id eq id) and (user eq userId) }
    }

    override fun getPokemonListFromBuild(id: Int, authId: String): List<GrownPokemon> {
        checkUserBuild(id, authId)
        val query = PokemonBuildMap.innerJoin(GrownPokemons).innerJoin(Abilities).innerJoin(Pokemons).innerJoin(Goods)
            .select { (PokemonBuildMap.build eq id) and (PokemonBuildMap.pokemon eq GrownPokemons.id) }
        return query.map { row -> convertGrownPokemon(row) }
    }

    override fun addPokemon(buildId: Int, pokemonId: Int, authId: String) {
        checkUserBuild(buildId, authId)
        checkGrownPokemon(authId, pokemonId)
        PokemonBuildMap.insert {
            it[build] = buildId
            it[pokemon] = pokemonId
        }
    }

    override fun getPokemonByIdFromBuild(buildId: Int, pokemonId: Int, authId: String): GrownPokemon {
        checkUserBuild(buildId, authId)
        checkGrownPokemon(authId, pokemonId)
        val query = PokemonBuildMap.innerJoin(GrownPokemons).innerJoin(Abilities).innerJoin(Pokemons).innerJoin(Goods)
            .select { (PokemonBuildMap.build eq buildId) and (PokemonBuildMap.pokemon eq pokemonId) }
        return query.map { row -> convertGrownPokemon(row) }[0]
    }

    override fun updateNickname(nickname: String, pokemonId: Int, authId: String) {
        checkGrownPokemon(authId, pokemonId)
        GrownPokemons.update({ GrownPokemons.id eq pokemonId }) {
            it[comment] = nickname
        }
    }

    override fun getPublicBuild(buildId: Int): Build {
        val isPublic =
            PublicBuilds.select { (PublicBuilds.build eq buildId) and (PublicBuilds.isPublic eq true) }.count() > 0
        if (!isPublic) {
            throw NotFoundException("公開されていないビルドです")
        }
        return getBuild(buildId)
    }

    override fun makeBuildPublic(buildId: Int, authId: String) {
        checkUserBuild(buildId, authId)
        val isExist = PublicBuilds.select { PublicBuilds.build eq buildId }.count() > 0
        if (!isExist) {
            PublicBuilds.insert {
                it[build] = buildId
                it[isPublic] = true
            }
        } else {
            PublicBuilds.update({ PublicBuilds.build eq buildId }) { it[isPublic] = true }
        }
    }

    override fun makeBuildPrivate(buildId: Int, authId: String) {
        checkUserBuild(buildId, authId)
        val isExist = PublicBuilds.select { PublicBuilds.build eq buildId }.count() > 0
        if (isExist) {
            PublicBuilds.update({ PublicBuilds.build eq buildId }) { it[isPublic] = false }
        }
    }

    override fun isPublicBuild(buildId: Int): Boolean {
        return PublicBuilds.select { (PublicBuilds.build eq buildId) and (PublicBuilds.isPublic eq true) }.count() > 0
    }

    override fun getPublicBuildList(): List<Int> =
        PublicBuilds.select { PublicBuilds.isPublic eq true }.map { it[PublicBuilds.build].value }

    override fun updateTerastal(id: Int, pokemonId: Int, authId: String) {
        checkGrownPokemon(authId, pokemonId)
        val isExist = TerastalMap.select { TerastalMap.pokemonId eq pokemonId }.count() > 0
        if (isExist) {
            TerastalMap.update({ TerastalMap.pokemonId eq pokemonId }) {
                it[type] = id
            }
        } else {
            TerastalMap.insert {
                it[TerastalMap.pokemonId] = pokemonId
                it[type] = id
            }
        }
    }

    override fun updateTerastalByValue(value: String, id: Int, authId: String) {
        if (value == "設定なし") {
            deleteTerastal(id, authId)
            return
        }
        val typeId = MasterCache.getTypeId(value)
        updateTerastal(typeId, id, authId)
    }

    private fun deleteTerastal(pokemonId: Int, authId: String) {
        checkGrownPokemon(authId, pokemonId)
        TerastalMap.deleteWhere { TerastalMap.pokemonId eq pokemonId }
    }

    private fun checkUserBuild(id: Int, authId: String) {
        val exist = PokemonBuilds.innerJoin(Users).select { (Users.authId eq authId) and (PokemonBuilds.id eq id) }
            .count() > 0
        if (!exist) {
            throw IllegalArgumentException("指定の構築が存在しません！")
        }
    }

    private fun getUserIdFromAuthId(authId: String) =
        Users.select { Users.authId eq authId }.map { row -> row[Users.id] }[0].value
}