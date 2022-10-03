package com.sasakirione.pokebuild.controller

import com.sasakirione.pokebuild.usecase.PokemonDataUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PokemonDataController : KoinComponent {
    private val useCase: PokemonDataUseCase by inject()

    fun getPokemon(id: Int) = useCase.getPokemonData(id)

    fun getGoods() = ResponseGoods(useCase.getGoods().map { ResponseGood(it.first, it.second) })

    fun getTags() = useCase.getTags()

    fun getMoves() = useCase.getMoves()

    fun getPokemonList() = useCase.getPokemonList()

    fun getPokemonMove(pokemonId: Int) = useCase.getPokemonMove(pokemonId)
}

data class ResponseGoods(
    val goods: List<ResponseGood>
)

data class ResponseGood(
    val id: Int,
    val name: String
)
