package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object PokemonMoveMap: IntIdTable("pokemon_move_map") {
    val pokemon = reference("pokemon", Pokemons)
    val move = reference("move", Moves)
    val version = reference("version", GameVersions)
}