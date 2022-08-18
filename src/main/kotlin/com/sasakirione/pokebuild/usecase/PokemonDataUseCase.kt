package com.sasakirione.pokebuild.usecase

import com.sasakirione.pokebuild.repository.IPokemonDataRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PokemonDataUseCase: KoinComponent {
    private val pokemonDataRepository: IPokemonDataRepository by inject()

    fun getPokemonNameList(input: String) = transaction {
        pokemonDataRepository.getPokemonNameList(input)
    }

    fun getPokemonData(id: Int) = transaction {
        pokemonDataRepository.getPokemon(id)
    }

    fun getGoods() = transaction {
        pokemonDataRepository.getGoods()
    }

    fun getTags() = transaction {
        pokemonDataRepository.getTags()
    }

    fun getMoves() = transaction {
        pokemonDataRepository.getMoves()
    }

    fun getPokemonList() = transaction {
        pokemonDataRepository.getPokemonList()
    }

    fun getBuild(authId: String) = transaction {
        pokemonDataRepository.getBuild(0, authId)
    }

}