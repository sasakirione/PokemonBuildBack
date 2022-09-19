package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object PublicBuilds : IntIdTable("public_builds") {
    val build = reference("build", PokemonBuilds)
    val isPublic = bool("is_public")
}