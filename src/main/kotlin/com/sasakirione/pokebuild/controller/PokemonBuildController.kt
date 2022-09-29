package com.sasakirione.pokebuild.controller

import com.sasakirione.pokebuild.domain.Build
import com.sasakirione.pokebuild.domain.BuildWithoutPokemonList
import com.sasakirione.pokebuild.domain.GrownPokemon
import com.sasakirione.pokebuild.usecase.PokemonBuildUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PokemonBuildController : KoinComponent {
    private val useCase: PokemonBuildUseCase by inject()

    fun insertPokemon(inParams: PostInsertPokemon, authId: String): ResponseInsertPokemon =
        ResponseInsertPokemon(useCase.insertPokemon(inParams.pokemon, inParams.buildId, authId))

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
                7 -> UpdateType.NICKNAME
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
                7 -> UpdateType.NICKNAME
                else -> throw IllegalArgumentException("存在しない項目を更新しようとしています")
            }
        )

    fun getGrownPokemon(id: Int, authId: String) =
        useCase.getGrownPokemon(id, authId)

    fun getGrownPokemonList(authId: String) =
        useCase.getGrownPokemonList(authId)

    fun insertGrownPokemon(inParams: GrownPokemon, authId: String) =
        useCase.insertGrownPokemon(inParams, authId)

    fun deletePokemonFromBuild(buildId: Int, pokemonId: Int, authId: String) =
        useCase.deletePokemonFromBuild(buildId, pokemonId, authId)

    fun createBuild(build: BuildWithoutPokemonList, authId: String) =
        useCase.createBuild(build, authId)

    fun updateBuild(id: Int, build: BuildWithoutPokemonList, authId: String) =
        useCase.updateBuild(id, build, authId)

    fun deleteBuild(id: Int, authId: String) =
        useCase.deleteBuild(id, authId)

    fun getPokemonListFromBuild(id: Int, authId: String) =
        useCase.getPokemonListFromBuild(id, authId)

    fun addPokemon(buildId: Int, pokemonId: Int, authId: String) =
        useCase.addPokemon(buildId, pokemonId, authId)

    fun getPokemonByIdFromBuild(buildId: Int, pokemonId: Int, authId: String) =
        useCase.getPokemonByIdFromBuild(buildId, pokemonId, authId)

    fun getPublicBuild(buildId: Int) =
        useCase.getPublicBuild(buildId)

    fun makePublicBuild(buildId: Int, authId: String) =
        useCase.makeBuildPublic(buildId, authId)

    fun makePrivateBuild(buildId: Int, authId: String) =
        useCase.makeBuildPrivate(buildId, authId)

    fun isPublicBuild(buildId: Int) =
        useCase.isPublicBuild(buildId)

    fun getPublicBuildList() =
        useCase.getPublicBuildList()
}

data class PostUpdateGrownPokemon(
    val ids: List<Int>,
    val itemSelect: Int
)

data class PostUpdateGrownPokemon2(
    val values: List<String>,
    val itemSelect: Int
)

data class ResponseInsertPokemon(val pokemonId: Int)

data class PostInsertPokemon(val pokemon: GrownPokemon, val buildId: Int)

enum class UpdateType {
    GOOD, EV, ABILITY, TAG, NATURE, MOVE, NICKNAME
}