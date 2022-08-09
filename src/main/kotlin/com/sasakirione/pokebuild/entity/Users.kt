package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object Users: IntIdTable() {
    val name = varchar("name", 50).default("ポケモントレーナー")
    val authId = varchar("auth_id", 255).uniqueIndex()
    val profile = text("profile")
    val icon = varchar("icon", 100).default("")
}