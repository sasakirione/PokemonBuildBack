package com.sasakirione.pokebuild.controller

import com.sasakirione.pokebuild.usecase.PokemonDataUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PokemonDataController: KoinComponent {
    private val useCase: PokemonDataUseCase by inject()

    fun getPokemonNameList(input: String) = PokemonNameList(useCase.getPokemonNameList(input))
    fun getPokemon(id: Int) = useCase.getPokemonData(id)

    fun getGoods() = ResponseGoods(useCase.getGoods().map {ResponseGood(it.first, it.second)})

    fun getTags() = useCase.getTags()

    fun getMoves() = useCase.getMoves()
}

data class PokemonNameList(
    val nameList: List<String>
)

data class ResponseGoods(
    val goods: List<ResponseGood>
)

data class ResponseGood (
    val id: Int,
    val name: String
)
