package com.sasakirione.pokebuild.domain

data class Pokemon(
    val id: Int,
    val dexNo: Int,
    val name: String,
    val formName: String?,
    val types: List<String>,
    val abilities: List<String>,
    val base: List<Int>,
)