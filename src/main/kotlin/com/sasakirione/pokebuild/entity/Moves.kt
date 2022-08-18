package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object Moves : IntIdTable() {
    val name = varchar("name", 30).uniqueIndex()
    val select = reference("select", MoveSelects)
    val type = reference("type", Types)
    val damage = integer("damage").nullable()
}