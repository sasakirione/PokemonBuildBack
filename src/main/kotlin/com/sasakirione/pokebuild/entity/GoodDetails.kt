package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object GoodDetails: IntIdTable("good_details") {
    val goodId = reference("good_id", Goods)
    val detail = text("detail")
}