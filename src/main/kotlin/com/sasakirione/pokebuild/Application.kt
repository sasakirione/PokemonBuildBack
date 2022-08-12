package com.sasakirione.pokebuild

import com.sasakirione.pokebuild.entity.*
import com.sasakirione.pokebuild.plugins.moduleA
import com.sasakirione.pokebuild.usecase.PokemonDataUseCase
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.environmentProperties
import org.koin.fileProperties
import org.koin.ktor.ext.getProperty
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@OptIn(KtorExperimentalLocationsAPI::class)
fun Application.module() {
    install(Koin) {
        fileProperties("/koin.properties")
        environmentProperties()
        modules(moduleA)
        koin.setProperty("db.url", environment.config.property("db.url").getString())
        koin.setProperty("db.user", environment.config.property("db.user").getString())
        koin.setProperty("db.password", environment.config.property("db.password").getString())
    }
    install(ContentNegotiation) {
        jackson()
    }
    install(Locations)
    Database.connect(
        url = getProperty("db.url") ?: "jdbc:postgresql://localhost:5432/pokemon",
        driver = "org.postgresql.Driver",
        user = getProperty("db.user") ?: "sasakirione",
        password = getProperty("db.password") ?: "password"
    )

    transaction {
        SchemaUtils.create(Abilities)
        SchemaUtils.create(GameVersions)
        SchemaUtils.create(Goods)
        SchemaUtils.create(GrownPokemons)
        SchemaUtils.create(Moves)
        SchemaUtils.create(MoveSelects)
        SchemaUtils.create(Natures)
        SchemaUtils.create(PokemonAbilityMap)
        SchemaUtils.create(PokemonBuildMap)
        SchemaUtils.create(PokemonBuilds)
        SchemaUtils.create(PokemonMoveMap)
        SchemaUtils.create(Pokemons)
        SchemaUtils.create(PokemonTagMap)
        SchemaUtils.create(PokemonTags)
        SchemaUtils.create(PokemonTypeMap)
        SchemaUtils.create(Types)
        SchemaUtils.create(Users)
    }
    routing {
        val pokemonDataUseCase: PokemonDataUseCase by inject()

        route("v1") {
            route("pokemon_data") {
                get("/{id}"){
                    val id = call.parameters["id"]?.toIntOrNull() ?:
                        return@get call.respond(HttpStatusCode.BadRequest)
                    call.respond(pokemonDataUseCase.getPokemonData(id))
                }
                route("suggest_list") {
                    get("/{input}"){
                        val input = call.parameters["input"] ?:
                            return@get call.respond(HttpStatusCode.BadRequest)
                        call.respond(pokemonDataUseCase.getPokemonNameList(input))
                    }
                }
            }
        }
    }

}
