package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.controller.Setting
import com.sasakirione.pokebuild.entity.UserSettings
import com.sasakirione.pokebuild.entity.Users
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class UserRepository: IUserRepository {
    override fun getSetting(authId: String): Setting {
        val userId = getUserIdFromAuthId(authId)
        val settingCount = UserSettings.select { UserSettings.userId eq userId }.count()
        return if (settingCount == 0L) {
            Setting(isUsedNickname = false)
        } else {
            val setting = UserSettings.select { UserSettings.userId eq userId }.toList()
            val isUsedNicknameRaw = setting.filter{ resultRow ->  resultRow[UserSettings.settingId] == 1 }[0][UserSettings.settingValue]
            val isUsedNickname = isUsedNicknameRaw == 1
            Setting(isUsedNickname = isUsedNickname)
        }
    }

    override fun setSetting(setting: Setting, authId: String) {
        val userId = getUserIdFromAuthId(authId)
        val settingList = UserSettings.select { UserSettings.userId eq userId }.toList()
        val existIsUsedNickname = settingList.any { resultRow -> resultRow[UserSettings.settingId] == 1 }
        if (!existIsUsedNickname) {
            UserSettings.insert {
                it[UserSettings.userId] = userId
                it[settingId] = 1
                it[settingValue] = if (setting.isUsedNickname) 1 else 0
            }
        } else {
            UserSettings.update({ UserSettings.userId eq userId and (UserSettings.settingId eq 1) }) {
                it[settingValue] = if (setting.isUsedNickname) 1 else 0
            }
        }
    }

    private fun getUserIdFromAuthId(authId: String) =
        Users.select { Users.authId eq authId }.map { row -> row[Users.id] }[0].value

}