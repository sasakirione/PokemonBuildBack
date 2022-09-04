package com.sasakirione.pokebuild.usecase

import com.sasakirione.pokebuild.domain.GrownPokemon
import com.sasakirione.pokebuild.repository.IPokemonBuildRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PokemonBuildUseCase : KoinComponent {
    private val pokemonBuildRepository: IPokemonBuildRepository by inject()

    fun getBuild(authId: String) = transaction {
        pokemonBuildRepository.checkUser(authId)
        pokemonBuildRepository.getBuild(0, authId)
    }

    fun insertPokemon(pokemon: GrownPokemon, buildId: Int, authId: String) = transaction {
        pokemonBuildRepository.insertPokemon(pokemon, buildId, authId)
    }

    fun updateGood(goodId: Int, pokemonId: Int, authId: String) = transaction {
        pokemonBuildRepository.updateGood(goodId, pokemonId, authId)
    }

    fun updateEv(ev: List<Int>, pokemonId: Int, authId: String) = transaction {
        pokemonBuildRepository.updateEv(ev, pokemonId, authId)
    }

    fun updateAbility(abilityName: String, pokemonId: Int, authId: String) = transaction {
        pokemonBuildRepository.updateAbility(abilityName, pokemonId, authId)
    }

    fun updateNature(natureName: String, pokemonId: Int, authId: String) = transaction {
        pokemonBuildRepository.updateNature(natureName, pokemonId, authId)
    }

    fun updateTags(tagNames: List<String>, pokemonId: Int, authId: String) = transaction {
        pokemonBuildRepository.updateTag(tagNames, pokemonId, authId)
    }

    fun updateMoves(moveNames: List<String>, pokemonId: Int, authId: String) = transaction {
        pokemonBuildRepository.updateMoves(moveNames, pokemonId, authId)
    }

    fun deletePokemon(pokemonId: Int, authId: String) = transaction {
        pokemonBuildRepository.deletePokemon(pokemonId, authId)
    }

    fun getBuildList(authId: String) = transaction {
        pokemonBuildRepository.checkUser(authId)
        pokemonBuildRepository.getBuildList(authId)
    }
}