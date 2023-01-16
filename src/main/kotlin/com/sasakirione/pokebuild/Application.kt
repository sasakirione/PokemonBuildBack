package com.sasakirione.pokebuild

import com.auth0.jwk.JwkProviderBuilder
import com.sasakirione.pokebuild.entity.*
import com.sasakirione.pokebuild.plugins.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.environmentProperties
import org.koin.fileProperties
import org.koin.ktor.ext.getProperty
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
        allowHeader("Content-Type")
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Put)
        allowCredentials = true
    }

    val issur = getProperty("auth0.issuer") ?: ""
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
    dbMigration()
    setCache()
    routing {
        route("v1") {
            pokemonDataRoute()
            pokemonBuildRoute()
            userRoute()
        }
    }
}

private fun dbMigration() = transaction {
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
    // v0.3.0
    SchemaUtils.create(UserSettings)
    SchemaUtils.create(PublicBuilds)
    // v0.4.0
    SchemaUtils.create(TerastalMap)
    SchemaUtils.create(GoodDetails)
}

private fun setCache() = transaction {
    MasterCache.abilities = Abilities.selectAll().map { it[Abilities.id].value to it[Abilities.name] }.toList()
    MasterCache.goods = Goods.innerJoin(GoodDetails).selectAll().map { Triple(it[Goods.id].value,it[Goods.name],it[GoodDetails.detail]) }.toList()
    MasterCache.moves = Moves.selectAll().map { it[Moves.id].value to it[Moves.name] }.toList()
    MasterCache.natures = Natures.selectAll().map { it[Natures.id].value to it[Natures.name] }.toList()
    MasterCache.tags = PokemonTags.selectAll().map { it[PokemonTags.id].value to it[PokemonTags.name] }.toList()
    MasterCache.types = Types.selectAll().map { it[Types.id].value to it[Types.name] }.toList()
    MasterCache.abilityMap =
        PokemonAbilityMap.selectAll().map { it[PokemonAbilityMap.pokemon].value to it[PokemonAbilityMap.ability].value }
            .toList()
    MasterCache.simplePokemons = Pokemons.selectAll().orderBy(Pokemons.dexNo)
        .map { it[Pokemons.id].value to (it[Pokemons.name] + " " + (it[Pokemons.formName] ?: "")) }.toList()
}
