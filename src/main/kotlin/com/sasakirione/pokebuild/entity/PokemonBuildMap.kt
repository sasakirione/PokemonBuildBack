package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object PokemonBuildMap : IntIdTable("pokemon_build_map") {
    val build = reference("build", PokemonBuilds)
    val pokemon = reference("pokemon", GrownPokemons)
    val isActive = bool("isActive").default(true)
    val comment = text("comment").nullable()
}