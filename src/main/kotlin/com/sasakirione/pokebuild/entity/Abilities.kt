package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object Abilities : IntIdTable() {
    val name = varchar("name", 20).uniqueIndex()
}