package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object Types : IntIdTable() {
    val name = varchar("name", 10).uniqueIndex()
}