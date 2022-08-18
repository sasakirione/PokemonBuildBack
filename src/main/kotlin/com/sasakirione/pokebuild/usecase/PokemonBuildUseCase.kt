package com.sasakirione.pokebuild.usecase

import com.sasakirione.pokebuild.repository.IPokemonBuildRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PokemonBuildUseCase : KoinComponent {
    private val pokemonBuildRepository: IPokemonBuildRepository by inject()

    fun getBuild(authId: String) = transaction {
        pokemonBuildRepository.getBuild(0, authId)
    }
}