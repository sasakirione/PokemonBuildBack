package com.sasakirione.pokebuild.plugins

import com.sasakirione.pokebuild.controller.PokemonBuildController
import com.sasakirione.pokebuild.controller.PokemonDataController
import com.sasakirione.pokebuild.repository.IPokemonBuildRepository
import com.sasakirione.pokebuild.repository.IPokemonDataRepository
import com.sasakirione.pokebuild.repository.PokemonBuildRepository
import com.sasakirione.pokebuild.repository.PokemonDataRepository
import com.sasakirione.pokebuild.usecase.PokemonBuildUseCase
import com.sasakirione.pokebuild.usecase.PokemonDataUseCase
import org.koin.dsl.module

val moduleA = module {
    single<IPokemonDataRepository> { PokemonDataRepository() }
    single<IPokemonBuildRepository> { PokemonBuildRepository() }
    single<PokemonDataUseCase> { PokemonDataUseCase() }
    single<PokemonBuildUseCase> { PokemonBuildUseCase() }
    single<PokemonDataController> { PokemonDataController() }
    single<PokemonBuildController> { PokemonBuildController() }
}