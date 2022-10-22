package com.sasakirione.pokebuild.plugins

object MasterCache {
    var abilities = listOf<Pair<Int, String>>()
    var goods = listOf<Pair<Int, String>>()
    var moves = listOf<Pair<Int, String>>()
    var natures = listOf<Pair<Int, String>>()
    var types = listOf<Pair<Int, String>>()
    var tags = listOf<Pair<Int, String>>()
    var abilityMap = listOf<Pair<Int, Int>>()
    var simplePokemons = listOf<Pair<Int, String>>()

    fun getPokemonAbilities(pokemonId: Int): List<String> =
        abilityMap.filter { it.first == pokemonId }.map { abilities.first { x -> x.first == it.second }.second }

    private fun getMoveId(moveName: String): Int =
        moves.first { it.second == moveName }.first

    fun getGoodId(goodName: String): Int =
        goods.first { it.second == goodName }.first

    fun getMoveIdList(moveNameList: List<String>): List<Int> =
        moveNameList.map { getMoveId(it) }

    fun getAbilityId(abilityName: String): Int =
        abilities.first { it.second == abilityName }.first

    fun getNatureId(natureName: String): Int =
        natures.first { it.second == natureName }.first

    fun getMoveNameList(moveIdList: List<Int>): List<String> =
        moveIdList.map { moves.first { x -> x.first == it }.second }

    fun getTypeId(terastal: String): Int {
        return types.first { it.second == terastal }.first
    }
}