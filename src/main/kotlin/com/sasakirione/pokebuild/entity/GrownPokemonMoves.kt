package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object GrownPokemonMoves: IntIdTable("grown_pokemon_moves") {
    val pokemonId = reference("pokemon_id", GrownPokemons)
    val moveId = reference("move_id", Moves)
    // 0: 必須技, 1: 任意技(採用), 2: 任意技(未採用)
    val moveSelectType = integer("move_select_type")
}