package com.sasakirione.pokebuild.plugins

import com.sasakirione.pokebuild.repository.IPokemonDataRepository
import com.sasakirione.pokebuild.repository.PokemonDataRepository
import com.sasakirione.pokebuild.usecase.PokemonDataUseCase
import org.koin.dsl.module

val moduleA = module {
    single<IPokemonDataRepository> { PokemonDataRepository() }
    single<PokemonDataUseCase> { PokemonDataUseCase() }
}