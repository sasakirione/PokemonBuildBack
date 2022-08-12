package com.sasakirione.pokebuild.controller

import com.sasakirione.pokebuild.usecase.PokemonDataUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PokemonDataController: KoinComponent {
    private val useCase: PokemonDataUseCase by inject()

    fun getPokemonNameList(input: String) = PokemonNameList(useCase.getPokemonNameList(input))
    fun getPokemon(id: Int) = useCase.getPokemonData(id)
}

data class PokemonNameList(
    val nameList: List<String>
)