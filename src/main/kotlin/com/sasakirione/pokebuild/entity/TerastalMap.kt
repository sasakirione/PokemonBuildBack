package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object TerastalMap : IntIdTable("terastal_map") {
    val pokemonId = reference("pokemon_id", GrownPokemons)
    val type = reference("type", Types)
}