package com.sasakirione.pokebuild.usecase

import com.sasakirione.pokebuild.controller.UpdateType
import com.sasakirione.pokebuild.domain.GrownPokemon
import com.sasakirione.pokebuild.repository.IPokemonBuildRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PokemonBuildUseCase : KoinComponent {
    private val pokemonBuildRepository: IPokemonBuildRepository by inject()

    fun getBuild(authId: String) = transaction {
        pokemonBuildRepository.checkUser(authId)
        val buildId = pokemonBuildRepository.getFirstBuildId(authId)
        pokemonBuildRepository.getBuild(buildId, authId)
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
        pokemonBuildRepository.updateAbilityByValue(abilityName, pokemonId, authId)
    }

    fun updateNature(natureName: String, pokemonId: Int, authId: String) = transaction {
        pokemonBuildRepository.updateNatureByValue(natureName, pokemonId, authId)
    }

    fun updateTags(tagNames: List<String>, pokemonId: Int, authId: String) = transaction {
        pokemonBuildRepository.updateTagByValue(tagNames, pokemonId, authId)
    }

    fun updateMoves(moveNames: List<String>, pokemonId: Int, authId: String) = transaction {
        pokemonBuildRepository.updateMovesByValue(moveNames, pokemonId, authId)
    }

    fun deletePokemon(pokemonId: Int, authId: String) = transaction {
        pokemonBuildRepository.deletePokemon(pokemonId, authId)
    }

    fun getBuildList(authId: String) = transaction {
        pokemonBuildRepository.checkUser(authId)
        pokemonBuildRepository.getBuildList(authId)
    }

    fun getBuildById(id: Int, authId: String) = transaction {
        pokemonBuildRepository.getBuild(id, authId)
    }

    fun updateGrownPokemonById(pokemonId: Int, authId: String, itemIds: List<Int>, updateType: UpdateType) = transaction {
        if (itemIds.isEmpty()) {throw IllegalArgumentException("IDが含まれていません！")}
        when (updateType) {
            UpdateType.GOOD -> pokemonBuildRepository.updateGood(itemIds[0], pokemonId, authId)
            UpdateType.EV -> pokemonBuildRepository.updateEv(itemIds, pokemonId, authId)
            UpdateType.ABILITY -> pokemonBuildRepository.updateAbility(itemIds[0], pokemonId, authId)
            UpdateType.NATURE -> pokemonBuildRepository.updateNature(itemIds[0], pokemonId, authId)
            UpdateType.TAG -> pokemonBuildRepository.updateTag(itemIds, pokemonId, authId)
            UpdateType.MOVE -> pokemonBuildRepository.updateMove(itemIds, pokemonId, authId)
        }
    }

    fun updateGrownPokemonByValue(id: Int, authId: String, values: List<String>, updateType: UpdateType) = transaction {
        if (values.isEmpty()) {throw IllegalArgumentException("値が含まれていません！")}
        when (updateType) {
            UpdateType.GOOD -> pokemonBuildRepository.updateGoodByValue(values[0], id, authId)
            UpdateType.EV -> throw IllegalArgumentException("EVはIDで更新してください！")
            UpdateType.ABILITY -> pokemonBuildRepository.updateAbilityByValue(values[0], id, authId)
            UpdateType.NATURE -> pokemonBuildRepository.updateNatureByValue(values[0], id, authId)
            UpdateType.TAG -> pokemonBuildRepository.updateTagByValue(values, id, authId)
            UpdateType.MOVE -> pokemonBuildRepository.updateMovesByValue(values, id, authId)
        }
    }
}