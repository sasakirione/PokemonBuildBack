package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object GameVersions: IntIdTable("game_versions") {
    val name = varchar("name", 30).uniqueIndex()
    val gen = integer("gen")
}