package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.domain.Build
import com.sasakirione.pokebuild.domain.Pokemon

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
    fun insertPokemon(pokemon: Pokemon, buildId: Int, authId: String)
}