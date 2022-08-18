package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object PokemonTags : IntIdTable("pokemon_tags") {
    val name = varchar("name", 15)
    val color = varchar("color", 50)
}