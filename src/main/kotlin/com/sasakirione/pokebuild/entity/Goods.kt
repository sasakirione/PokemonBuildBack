package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object Goods : IntIdTable() {
    val name = varchar("name", 20).uniqueIndex()
}