package com.sasakirione.pokebuild

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.algorithms.Algorithm
import com.sasakirione.pokebuild.controller.PokemonBuildController
import com.sasakirione.pokebuild.controller.PokemonDataController
import com.sasakirione.pokebuild.entity.*
import com.sasakirione.pokebuild.plugins.moduleA
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
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
import java.util.concurrent.TimeUnit

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {
    install(Koin) {
        fileProperties("/koin.properties")
        environmentProperties()
        modules(moduleA)
        koin.setProperty("db.url", environment.config.property("db.url").getString())
        koin.setProperty("db.user", environment.config.property("db.user").getString())
        koin.setProperty("db.password", environment.config.property("db.password").getString())
        koin.setProperty("auth0.issuer", environment.config.property("auth0.issuer").getString())
        koin.setProperty("auth0.audience", environment.config.property("auth0.audience").getString())
        koin.setProperty("auth0.secret", environment.config.property("auth0.secret").getString())
    }
    install(ContentNegotiation) {
        jackson()
    }
    install(CORS)
    {
        hosts += "*"
        allowHeader("*")
        anyHost()
        allowHeader(HttpHeaders.Authorization)
        allowHeader("CrossDomain")
        allowHeader("X-CSRF-Token")
        allowHeader("authorization")
        allowMethod(HttpMethod.Options)
        allowCredentials = true
    }

    val issur = getProperty("auth0.issuer") ?: ""
    val audience = getProperty("auth0.audience") ?: ""
    val algorithm: Algorithm = Algorithm.HMAC256(getProperty("auth0.secret") ?: "")
    val jwkProvider = JwkProviderBuilder(issur)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    install(Authentication) {
        jwt {
            realm = "Ktor Server"
            verifier(jwkProvider, issur)
            validate { credential ->
                JWTPrincipal(credential.payload)
            }
        }
    }
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
        val pokemonDataController: PokemonDataController by inject()
        val pokemonBuildController: PokemonBuildController by inject()

        route("v1") {
            route("pokemon_data") {
                get("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                    call.respond(pokemonDataController.getPokemon(id))
                }
                route("suggest_list") {
                    get("/{input}") {
                        val input = call.parameters["input"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                        call.respond(pokemonDataController.getPokemonNameList(input))
                    }
                }
                get("get_goods") {
                    call.respond(pokemonDataController.getGoods())
                }
                get("get_tags") {
                    call.respond(pokemonDataController.getTags())
                }
                get("get_moves") {
                    call.respond(pokemonDataController.getMoves())
                }
                get("pokemon_list") {
                    call.respond(pokemonDataController.getPokemonList())
                }
            }
            authenticate {
                route("pokemon_build") {
                    get("get_build") {
                        val principal = call.authentication.principal<JWTPrincipal>()
                        val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@get call.respond(
                            HttpStatusCode.BadRequest
                        )
                        call.respond(pokemonBuildController.getBuild(authId))
                    }
                }
            }
        }
    }

}
