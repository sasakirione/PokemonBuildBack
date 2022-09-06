package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.domain.Build
import com.sasakirione.pokebuild.domain.BuildWithoutPokemonList
import com.sasakirione.pokebuild.domain.GrownPokemon

interface IPokemonBuildRepository {
    /**
     * 構築情報を取得する
     *
     * @param id 指定があれば構築ID、なければ0
     * @param authId 対象のユーザーのAuth0のID
     * @return 構築情報
     */
    fun getBuild(id: Int = 0, authId: String): Build

    /**
     * 構築にポケモンを追加する
     *
     * @param pokemon ポケモンの情報
     * @param buildId 構築ID
     * @param authId 対象のユーザーのAuth0のID
     */
    fun insertPokemon(pokemon: GrownPokemon, buildId: Int, authId: String): Int

    /**
     * 道具を更新する
     *
     * @param goodId 更新後の道具ID
     * @param pokemonId 道具を更新するポケモンのID
     * @param authId 対象のユーザーのAuth0のID
     */
    fun updateGood(goodId: Int, pokemonId: Int, authId: String)

    /**
     * 努力値を更新する
     *
     * @param ev
     * @param pokemonId
     * @param authId
     */
    fun updateEv(ev: List<Int>, pokemonId: Int, authId: String)

    fun updateAbilityByValue(abilityName: String, pokemonId: Int, authId: String)

    fun updateTagByValue(tagNames: List<String>, pokemonId: Int, authId: String)

    fun updateNatureByValue(natureName: String, pokemonId: Int, authId: String)

    fun updateMovesByValue(moveNames: List<String>, pokemonId: Int, authId: String)

    fun checkUser(authId: String)

    fun deletePokemon(pokemonId: Int, authId: String)

    fun getBuildList(authId: String): List<BuildWithoutPokemonList>

    fun getFirstBuildId(authId: String): Int

    fun updateAbility(abilityId: Int, pokemonId: Int, authId: String)

    fun updateNature(natureId: Int, pokemonId: Int, authId: String)

    fun updateTag(tagId: List<Int>, pokemonId: Int, authId: String)

    fun updateMove(moveIds: List<Int>, pokemonId: Int, authId: String)

    fun updateGoodByValue(goodName: String, pokemonId: Int, authId: String)
    fun getGrownPokemon(pokemonId: Int, authId: String): GrownPokemon

    fun getGrownPokemonList(authId: String) : List<GrownPokemon>

    fun insertGrownPokemon(pokemon: GrownPokemon, authId: String): Int

    fun deletePokemonFromBuild(buildId: Int, pokemonId: Int, authId: String)
    fun createBuild(build: BuildWithoutPokemonList, authId: String)
    fun updateBuild(id: Int, build: BuildWithoutPokemonList, authId: String)
    fun deleteBuild(id: Int, authId: String)

    fun getPokemonListFromBuild(id: Int, authId: String) : List<GrownPokemon>
    fun addPokemon(buildId: Int, pokemonId: Int, authId: String)
    fun getPokemonByIdFromBuild(buildId: Int, pokemonId: Int, authId: String) : GrownPokemon
}