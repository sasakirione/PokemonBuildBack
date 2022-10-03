package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.domain.Pokemon

interface IPokemonDataRepository {

    /**
     * ポケモンの情報を取得する
     *
     * @param id ポケモンID
     * @return ポケモンの情報
     */
    fun getPokemon(id: Int): Pokemon

    /**
     * 道具の一覧をIDつきで取得する
     *
     * @return 道具名とIDのTupleのリスト
     */
    fun getGoods(): List<Pair<Int, String>>

    /**
     * ポケモン用のタグ一覧を取得する
     *
     * @return タグ名のリスト
     */
    fun getTags(): List<String>

    /**
     * ポケモンの技一覧を取得する
     *
     * @return 技名とIDのTupleのリスト
     */
    fun getMoves(): List<Pair<Int, String>>

    /**
     * ポケモンの一覧をID付きで取得する
     *
     * @return ポケモン名とIDのTupleのリスト
     */
    fun getPokemonList(): List<Pair<Int, String>>

    fun getPokemonMove(pokemonId: Int): List<Int>
}