package com.sasakirione.pokebuild.domain

data class GrownPokemon(
    val id: Int,
    val personalId: Int,
    val name: String,
    val iv: List<Int>,
    val ev: List<Int>,
    val nature: Int,
    val ability: String,
    val abilityList: List<String>,
    val bv: List<Int>,
    val moveList: List<String>,
    val good: String,
    val tag: List<String>,
    val nickname: String?
)
