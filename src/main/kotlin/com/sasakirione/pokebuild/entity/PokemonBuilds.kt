package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object PokemonBuilds : IntIdTable("pokemon_builds") {
    val name = varchar("name", 100)
    val comment = text("comment").nullable()
    val user = reference("user", Users)
}