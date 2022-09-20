package com.sasakirione.pokebuild.usecase

import com.sasakirione.pokebuild.controller.Setting
import com.sasakirione.pokebuild.repository.IPokemonBuildRepository
import com.sasakirione.pokebuild.repository.IUserRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserUsaCase : KoinComponent {
    private val userRepository: IUserRepository by inject()
    private val pokemonBuildRepository: IPokemonBuildRepository by inject()

    fun getSetting(authId: String): Setting = transaction {
        pokemonBuildRepository.checkUser(authId)
        userRepository.getSetting(authId)
    }

    fun updateSetting(authId: String, setting: Setting) = transaction {
        pokemonBuildRepository.checkUser(authId)
        userRepository.setSetting(setting, authId)
    }
}