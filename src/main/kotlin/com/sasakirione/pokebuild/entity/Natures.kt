package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object Natures: IntIdTable() {
    val name = varchar("name", 10).uniqueIndex()
    val isMajime = bool("is_majime").default(false)
    val up = integer("up")
    val down = integer("down")
}