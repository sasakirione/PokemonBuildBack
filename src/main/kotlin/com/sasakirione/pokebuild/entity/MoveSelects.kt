package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object MoveSelects : IntIdTable("move_selects") {
    val name = varchar("name", 10).uniqueIndex()
}