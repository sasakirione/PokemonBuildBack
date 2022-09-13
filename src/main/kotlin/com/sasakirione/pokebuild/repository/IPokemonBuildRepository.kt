package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.domain.Build
import com.sasakirione.pokebuild.domain.BuildWithoutPokemonList
import com.sasakirione.pokebuild.domain.GrownPokemon

interface IPokemonBuildRepository {
    /**
     * 指定された構築情報を取得する
     *
     * @param id 構築ID
     * @param authId 認証ID
     * @return 構築情報
     */
    fun getBuild(id: Int, authId: String): Build

    /**
     * 構築に新規ポケモンを追加する
     *
     * @param pokemon 新規ポケモンの情報
     * @param buildId 構築ID
     * @param authId 認証ID
     */
    fun insertPokemon(pokemon: GrownPokemon, buildId: Int, authId: String): Int

    /**
     * 指定された育成済みポケモンの道具を名前で変更する
     *
     * @param goodId 道具ID
     * @param pokemonId 育成済みポケモンID
     * @param authId 認証ID
     */
    fun updateGood(goodId: Int, pokemonId: Int, authId: String)

    /**
     * 指定された育成済みポケモンの努力値を更新する
     *
     * @param ev 努力値のリスト(6個！)
     * @param pokemonId 育成済みポケモンID
     * @param authId 認証ID
     */
    fun updateEv(ev: List<Int>, pokemonId: Int, authId: String)

    /**
     * 指定された育成済みポケモンの特性を名前で変更する
     *
     * @param abilityName 特性の名前
     * @param pokemonId 育成済みポケモンID
     * @param authId 認証ID
     */
    fun updateAbilityByValue(abilityName: String, pokemonId: Int, authId: String)

    /**
     * 指定された育成済みポケモンのタグを名前で変更する
     *
     * @param tagNames タグ名の変更
     * @param pokemonId 育成済みポケモンID
     * @param authId 認証ID
     */
    fun updateTagByValue(tagNames: List<String>, pokemonId: Int, authId: String)

    /**
     * 指定された育成済みポケモンの性格を名前で変更する
     *
     * @param natureName 性格の名前
     * @param pokemonId 育成済みポケモンID
     * @param authId 認証ID
     */
    fun updateNatureByValue(natureName: String, pokemonId: Int, authId: String)

    /**
     * 指定された育成済みポケモンの技を名前で変更する
     *
     * @param moveNames 技の名前のリスト(4個！)
     * @param pokemonId 育成済みポケモンID
     * @param authId 認証ID
     */
    fun updateMovesByValue(moveNames: List<String>, pokemonId: Int, authId: String)

    /**
     * ユーザーの存在の有無を確認する(ダメだったら例外)
     *
     * @param authId 認証ID
     */
    fun checkUser(authId: String)

    /**
     * Delete pokemon
     *
     * @param pokemonId
     * @param authId
     */
    fun deletePokemon(pokemonId: Int, authId: String)

    /**
     * ユーザーの構築一覧を取得する
     *
     * @param authId 認証ID
     * @return 構築のリスト
     */
    fun getBuildList(authId: String): List<BuildWithoutPokemonList>

    /**
     * ユーザーの構築の中から一つ構築を取得する
     *
     * @param authId 認証ID
     * @return 構築ID
     */
    fun getFirstBuildId(authId: String): Int

    /**
     * 指定された育成済みポケモンの特性を変更する
     *
     * @param abilityId 特性ID
     * @param pokemonId 育成済みポケモンID
     * @param authId 認証ID
     */
    fun updateAbility(abilityId: Int, pokemonId: Int, authId: String)

    /**
     * 指定された育成済みポケモンの性格を変更する
     *
     * @param natureId 性格ID
     * @param pokemonId 育成済みポケモンID
     * @param authId 認証ID
     */
    fun updateNature(natureId: Int, pokemonId: Int, authId: String)

    /**
     * 指定された育成済みポケモンのタグを変更する(上書きです)
     *
     * @param tagId タグのID
     * @param pokemonId 育成済みポケモンID
     * @param authId 認証ID
     */
    fun updateTag(tagId: List<Int>, pokemonId: Int, authId: String)

    /**
     * 指定された育成済みポケモンの技を変更する(上書きです)
     *
     * @param moveIds 技のID(4つ)
     * @param pokemonId 育成済みポケモンID
     * @param authId 認証ID
     */
    fun updateMove(moveIds: List<Int>, pokemonId: Int, authId: String)

    /**
     * 指定された育成済みポケモンの道具を名前により変更する
     *
     * @param goodName 変更後の道具名
     * @param pokemonId 育成済みポケモンID
     * @param authId 認証ID
     */
    fun updateGoodByValue(goodName: String, pokemonId: Int, authId: String)

    /**
     * 指定された育成済みポケモンの詳細情報を取得する
     *
     * @param pokemonId 育成済みポケモンID
     * @param authId 認証ID
     * @return 育成済みポケモンの詳細情報
     */
    fun getGrownPokemon(pokemonId: Int, authId: String): GrownPokemon

    /**
     * 指定されたユーザーの育成済みポケモン一覧を取得する
     *
     * @param authId 認証ID
     * @return 育成済みポケモンの詳細情報のリスト
     */
    fun getGrownPokemonList(authId: String): List<GrownPokemon>

    /**
     * 育成済みポケモンを新規登録する(構築への紐付けは行わない)
     *
     * @param pokemon 新規育成済みポケモンの情報
     * @param authId 認証ID
     * @return 登録された育成済みポケモンのID
     */
    fun insertGrownPokemon(pokemon: GrownPokemon, authId: String): Int

    /**
     * 指定された育成済みポケモンを指定された構築から取り除く(ポケモン自体は削除されない)
     *
     * @param buildId 構築ID
     * @param pokemonId 育成済みポケモンID
     * @param authId 認証ID
     */
    fun deletePokemonFromBuild(buildId: Int, pokemonId: Int, authId: String)

    /**
     * 構築を作成する
     *
     * @param build 作成する構築情報
     * @param authId 認証ID
     */
    fun createBuild(build: BuildWithoutPokemonList, authId: String): Int

    /**
     * 指定された構築の構築情報(名前、コメント等)を更新する
     *
     * @param id 構築ID
     * @param build 更新後構築情報
     * @param authId 認証ID
     */
    fun updateBuild(id: Int, build: BuildWithoutPokemonList, authId: String)

    /**
     * 指定された構築を削除する
     *
     * @param id 構築ID
     * @param authId 認証ID
     */
    fun deleteBuild(id: Int, authId: String)

    /**
     * 指定された構築に登録されているポケモンの詳細情報一覧を取得する
     *
     * @param id 構築ID
     * @param authId 認証ID
     * @return ポケモンの詳細情報のリスト
     */
    fun getPokemonListFromBuild(id: Int, authId: String): List<GrownPokemon>

    /**
     * 既に登録済みの育成済みポケモンを指定の構築に追加する
     *
     * @param buildId 構築ID
     * @param pokemonId 育成済みポケモンID
     * @param authId 認証ID
     */
    fun addPokemon(buildId: Int, pokemonId: Int, authId: String)

    /**
     * 指定の構築から指定の育成済みポケモンの情報を取得する
     *
     * @param buildId 構築ID
     * @param pokemonId 育成済みポケモンID
     * @param authId 認証ID
     * @return 指定の育成済みポケモンの情報
     */
    fun getPokemonByIdFromBuild(buildId: Int, pokemonId: Int, authId: String): GrownPokemon
}