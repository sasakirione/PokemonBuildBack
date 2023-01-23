package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.controller.Setting
import com.sasakirione.pokebuild.entity.UserSettings
import com.sasakirione.pokebuild.entity.Users
import org.jetbrains.exposed.sql.*

class UserRepository : IUserRepository {
    override fun getSetting(authId: String): Setting {
        val userId = getUserIdFromAuthId(authId)
        val setting = UserSettings.select { UserSettings.userId eq userId }.toList()

        val isUsedNicknameItem = getSettingItem(setting, 1)
        val isUsedNickname = isUsedNicknameItem == 1

        return Setting(isUsedNickname = isUsedNickname)
    }

    override fun setSetting(setting: Setting, authId: String) {
        val userId = getUserIdFromAuthId(authId)
        val settingList = UserSettings.select { UserSettings.userId eq userId }.toList()
        setIsUsedNicknameSetting(settingList, userId, setting)
    }

    private fun getSettingItem(settingList: List<ResultRow>, settingId: Int): Int {
        val target = settingList.filter { resultRow -> resultRow[UserSettings.settingId] == settingId }
        if (target.isEmpty()) {
            return 0
        }
        return target[0][UserSettings.settingValue]
    }

    private fun setIsUsedNicknameSetting(
        settingList: List<ResultRow>,
        userId: Int,
        setting: Setting
    ) {
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