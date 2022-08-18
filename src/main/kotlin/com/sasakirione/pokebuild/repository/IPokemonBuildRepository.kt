package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.domain.Build
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

    fun updateAbility(abilityName: String, pokemonId: Int, authId: String)

    fun updateTag(tagNames: List<String>, pokemonId: Int, authId: String)

    fun updateNature(natureName: String, pokemonId: Int, authId: String)

    fun updateMoves(moveNames: List<String>, pokemonId: Int, authId: String)
}