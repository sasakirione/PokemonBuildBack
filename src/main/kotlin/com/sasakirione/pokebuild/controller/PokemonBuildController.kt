package com.sasakirione.pokebuild.controller

import com.sasakirione.pokebuild.domain.Build
import com.sasakirione.pokebuild.usecase.PokemonBuildUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PokemonBuildController : KoinComponent {
    private val useCase: PokemonBuildUseCase by inject()

    fun getBuild(authId: String): Build {
        return useCase.getBuild(authId)
    }
}