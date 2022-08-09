package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object Pokemons: IntIdTable() {
    val name = varchar("name", 10)
    val formName = varchar("form_name", 20).nullable()
    val dexNo = integer("dex_no")
    val form = integer("form_no").default(1)
    val baseH = integer("base_h")
    val baseA = integer("base_a")
    val baseB = integer("base_b")
    val baseC = integer("base_c")
    val baseD = integer("base_d")
    val baseS = integer("base_s")
    val icon = varchar("icon", 100)
    val icon2 = varchar("icon2", 100).nullable()
    val isRestriction = bool("is_restriction").default(false)
}