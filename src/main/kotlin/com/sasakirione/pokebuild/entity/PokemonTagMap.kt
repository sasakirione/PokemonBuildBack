package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object PokemonTagMap : IntIdTable("pokemon_tag_map") {
    val pokemon = reference("pokemon", Pokemons)
    val tag = reference("tag", PokemonTags)
}