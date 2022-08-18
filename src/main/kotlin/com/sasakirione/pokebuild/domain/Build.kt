package com.sasakirione.pokebuild.domain

data class Build(
    val id: Int,
    val name: String,
    val pokemons: List<GrownPokemon>
)
