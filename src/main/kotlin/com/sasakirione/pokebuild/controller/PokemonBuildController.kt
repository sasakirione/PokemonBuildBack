package com.sasakirione.pokebuild.controller

import com.sasakirione.pokebuild.domain.Build
import com.sasakirione.pokebuild.domain.BuildWithoutPokemonList
import com.sasakirione.pokebuild.domain.GrownPokemon
import com.sasakirione.pokebuild.usecase.PokemonBuildUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PokemonBuildController : KoinComponent {
    private val useCase: PokemonBuildUseCase by inject()

    fun getBuild(authId: String): Build {
        return useCase.getBuild(authId)
    }

    fun insertPokemon(inParams: PostInsertPokemon, authId: String): ResponseInsertPokemon =
        ResponseInsertPokemon(useCase.insertPokemon(inParams.pokemon, inParams.buildId, authId))

    fun updateGood(inParams: PostUpdateGood, authId: String) =
        useCase.updateGood(inParams.goodId, inParams.pokemonId, authId)

    fun updateEv(inParams: PostUpdateEv, authId: String) =
        useCase.updateEv(inParams.ev, inParams.pokemonId, authId)

    fun updateAbility(inParams: PostUpdateAbility, authId: String) =
        useCase.updateAbility(inParams.ability, inParams.pokemonId, authId)

    fun updateTag(inParams: PostUpdateTag, authId: String) =
        useCase.updateTags(inParams.tags, inParams.pokemonId, authId)

    fun updateNature(inParams: PostUpdateNature, authId: String) =
        useCase.updateNature(inParams.nature, inParams.pokemonId, authId)

    fun updateMoves(inParams: PostUpdateMoves, authId: String) =
        useCase.updateMoves(inParams.moves, inParams.pokemonId, authId)

    fun deletePokemon(inParams: PostDeletePokemon, authId: String) =
        useCase.deletePokemon(inParams.pokemonId, authId)

    fun deletePokemon(id: Int, authId: String) =
        useCase.deletePokemon(id, authId)

    fun getBuildList(authId: String): List<BuildWithoutPokemonList> {
        return useCase.getBuildList(authId)
    }

    fun getBuildById(id: Int, authId: String): Build = useCase.getBuildById(id, authId)

    fun updateGrownPokemonById(id: Int, inParams: PostUpdateGrownPokemon, authId: String) =
        useCase.updateGrownPokemonById(
            id, authId, inParams.ids, when (inParams.itemSelect) {
                1 -> UpdateType.ABILITY
                2 -> UpdateType.EV
                3 -> UpdateType.GOOD
                4 -> UpdateType.MOVE
                5 -> UpdateType.NATURE
                6 -> UpdateType.TAG
                else -> throw IllegalArgumentException("存在しない項目を更新しようとしています")
            }
        )

    fun updateGrownPokemonByValue(id: Int, inParams: PostUpdateGrownPokemon2, authId: String) =
        useCase.updateGrownPokemonByValue(
            id, authId, inParams.values, when (inParams.itemSelect) {
                1 -> UpdateType.ABILITY
                2 -> UpdateType.EV
                3 -> UpdateType.GOOD
                4 -> UpdateType.MOVE
                5 -> UpdateType.NATURE
                6 -> UpdateType.TAG
                else -> throw IllegalArgumentException("存在しない項目を更新しようとしています")
            }
        )
}

data class PostUpdateGrownPokemon(
    val ids: List<Int>,
    val itemSelect: Int
)

data class PostUpdateGrownPokemon2(
    val values: List<String>,
    val itemSelect: Int
)

data class PostUpdateGood(
    val goodId: Int,
    val pokemonId: Int
)

data class PostUpdateEv(
    val ev: List<Int>,
    val pokemonId: Int
)

data class PostUpdateAbility(
    val ability: String,
    val pokemonId: Int
)

data class PostUpdateTag(
    val tags: List<String>,
    val pokemonId: Int
)

data class PostUpdateNature(
    val nature: String,
    val pokemonId: Int
)

data class PostUpdateMoves(
    val moves: List<String>,
    val pokemonId: Int
)

data class PostDeletePokemon(
    val pokemonId: Int
)

data class ResponseInsertPokemon(val pokemonId: Int)

data class PostInsertPokemon(val pokemon: GrownPokemon, val buildId: Int)

enum class UpdateType {
    GOOD, EV, ABILITY, TAG, NATURE, MOVE
}