package com.sasakirione.pokebuild.plugins

import com.sasakirione.pokebuild.controller.PokemonBuildController
import com.sasakirione.pokebuild.controller.PokemonDataController
import com.sasakirione.pokebuild.controller.UserController
import com.sasakirione.pokebuild.repository.*
import com.sasakirione.pokebuild.usecase.PokemonBuildUseCase
import com.sasakirione.pokebuild.usecase.PokemonDataUseCase
import com.sasakirione.pokebuild.usecase.UserUsaCase
import org.koin.dsl.module

val moduleA = module {
    single<IPokemonDataRepository> { PokemonDataRepository() }
    single<IPokemonBuildRepository> { PokemonBuildRepository() }
    single<IUserRepository> { UserRepository() }
    single<PokemonDataUseCase> { PokemonDataUseCase() }
    single<PokemonBuildUseCase> { PokemonBuildUseCase() }
    single<UserUsaCase> { UserUsaCase() }
    single<PokemonDataController> { PokemonDataController() }
    single<PokemonBuildController> { PokemonBuildController() }
    single<UserController> { UserController() }
}