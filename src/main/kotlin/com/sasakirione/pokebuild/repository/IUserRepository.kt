package com.sasakirione.pokebuild.repository

import com.sasakirione.pokebuild.controller.Setting

interface IUserRepository {
    fun getSetting(authId: String): Setting
    fun setSetting(setting: Setting, authId: String)
}