package com.sasakirione.pokebuild.usecase

import com.sasakirione.pokebuild.controller.UpdateType
import com.sasakirione.pokebuild.domain.BuildWithoutPokemonList
import com.sasakirione.pokebuild.domain.GrownPokemon
import com.sasakirione.pokebuild.repository.IPokemonBuildRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PokemonBuildUseCase : KoinComponent {
    private val pokemonBuildRepository: IPokemonBuildRepository by inject()

    fun insertPokemon(pokemon: GrownPokemon, buildId: Int, authId: String) = transaction {
        pokemonBuildRepository.insertPokemon(pokemon, buildId, authId)
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

    fun updateGrownPokemonById(pokemonId: Int, authId: String, itemIds: List<Int>, updateType: UpdateType) =
        transaction {
            if (itemIds.isEmpty()) {
                throw IllegalArgumentException("IDが含まれていません！")
            }
            when (updateType) {
                UpdateType.GOOD -> pokemonBuildRepository.updateGood(itemIds[0], pokemonId, authId)
                UpdateType.EV -> pokemonBuildRepository.updateEv(itemIds, pokemonId, authId)
                UpdateType.ABILITY -> pokemonBuildRepository.updateAbility(itemIds[0], pokemonId, authId)
                UpdateType.NATURE -> pokemonBuildRepository.updateNature(itemIds[0], pokemonId, authId)
                UpdateType.TAG -> pokemonBuildRepository.updateTag(itemIds, pokemonId, authId)
                UpdateType.MOVE -> pokemonBuildRepository.updateMove(itemIds, pokemonId, authId)
                UpdateType.NICKNAME -> throw IllegalArgumentException("ニックネームはIDで更新できません！")
            }
        }

    fun updateGrownPokemonByValue(id: Int, authId: String, values: List<String>, updateType: UpdateType) = transaction {
        if (values.isEmpty()) {
            throw IllegalArgumentException("値が含まれていません！")
        }
        when (updateType) {
            UpdateType.GOOD -> pokemonBuildRepository.updateGoodByValue(values[0], id, authId)
            UpdateType.EV -> throw IllegalArgumentException("EVはIDで更新してください！")
            UpdateType.ABILITY -> pokemonBuildRepository.updateAbilityByValue(values[0], id, authId)
            UpdateType.NATURE -> pokemonBuildRepository.updateNatureByValue(values[0], id, authId)
            UpdateType.TAG -> pokemonBuildRepository.updateTagByValue(values, id, authId)
            UpdateType.MOVE -> pokemonBuildRepository.updateMovesByValue(values, id, authId)
            UpdateType.NICKNAME -> pokemonBuildRepository.updateNickname(values[0], id, authId)
        }
    }

    fun getGrownPokemon(id: Int, authId: String) = transaction {
        pokemonBuildRepository.getGrownPokemon(id, authId)
    }

    fun getGrownPokemonList(authId: String) = transaction {
        pokemonBuildRepository.getGrownPokemonList(authId)
    }

    fun insertGrownPokemon(pokemon: GrownPokemon, authId: String) = transaction {
        pokemonBuildRepository.insertGrownPokemon(pokemon, authId)
    }

    fun deletePokemonFromBuild(buildId: Int, pokemonId: Int, authId: String) = transaction {
        pokemonBuildRepository.deletePokemonFromBuild(buildId, pokemonId, authId)
    }

    fun createBuild(build: BuildWithoutPokemonList, authId: String) = transaction {
        pokemonBuildRepository.createBuild(build, authId)
    }

    fun updateBuild(id: Int, build: BuildWithoutPokemonList, authId: String) = transaction {
        pokemonBuildRepository.updateBuild(id, build, authId)
    }

    fun deleteBuild(id: Int, authId: String) = transaction {
        pokemonBuildRepository.deleteBuild(id, authId)
    }

    fun getPokemonListFromBuild(id: Int, authId: String) = transaction {
        pokemonBuildRepository.getPokemonListFromBuild(id, authId)
    }

    fun addPokemon(buildId: Int, pokemonId: Int, authId: String) = transaction {
        pokemonBuildRepository.addPokemon(buildId, pokemonId, authId)
    }

    fun getPokemonByIdFromBuild(buildId: Int, pokemonId: Int, authId: String) = transaction {
        pokemonBuildRepository.getPokemonByIdFromBuild(buildId, pokemonId, authId)
    }

    fun getPublicBuild(buildId: Int) = transaction {
        pokemonBuildRepository.getPublicBuild(buildId)
    }

    fun makeBuildPublic(buildId: Int, authId: String) = transaction {
        pokemonBuildRepository.makeBuildPublic(buildId, authId)
    }

    fun makeBuildPrivate(buildId: Int, authId: String) = transaction {
        pokemonBuildRepository.makeBuildPrivate(buildId, authId)
    }

    fun isPublicBuild(buildId: Int) = transaction {
        pokemonBuildRepository.isPublicBuild(buildId)
    }

    fun getPublicBuildList() = transaction {
        pokemonBuildRepository.getPublicBuildList()
    }

}