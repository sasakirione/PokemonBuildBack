package com.sasakirione.pokebuild.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object UserSettings : IntIdTable("user_settings") {
    val userId = reference("user_id", Users)
    val settingId = integer("setting_id")
    val settingValue = integer("setting_value")
}