package com.sasakirione.pokebuild.controller

import com.sasakirione.pokebuild.usecase.UserUsaCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserController : KoinComponent {
    private val useCase: UserUsaCase by inject()

    fun getSetting(authId: String) = useCase.getSetting(authId)

    fun updateSetting(authId: String, setting: Setting) = useCase.updateSetting(authId, setting)
}

data class Setting(
    val isUsedNickname: Boolean,
    val isDoubleBattle1: Boolean
)