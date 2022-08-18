package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object PokemonTypeMap : IntIdTable("pokemon_type_map") {
    var pokemon = reference("pokemon", Pokemons)
    var type = reference("type", Types)
}