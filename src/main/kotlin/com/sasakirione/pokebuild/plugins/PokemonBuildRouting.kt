package com.sasakirione.pokebuild.plugins

import com.sasakirione.pokebuild.controller.*
import com.sasakirione.pokebuild.domain.GrownPokemon
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.pokemonBuildRoute() {
    val pokemonBuildController: PokemonBuildController by inject()

    authenticate {
        route("pokemon_build") {
            get("get_build") {
                val principal = call.authentication.principal<JWTPrincipal>()
                val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@get call.respond(
                    HttpStatusCode.BadRequest
                )
                call.respond(pokemonBuildController.getBuild(authId))
            }
            get("get_build/{id}") {
                val id =
                    call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                val principal = call.authentication.principal<JWTPrincipal>()
                val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@get call.respond(
                    HttpStatusCode.BadRequest
                )
                call.respond(pokemonBuildController.getBuildById(id, authId))
            }

            get("get_builds") {
                val principal = call.authentication.principal<JWTPrincipal>()
                val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@get call.respond(
                    HttpStatusCode.BadRequest
                )
                call.respond(pokemonBuildController.getBuildList(authId))
            }

            post("post_pokemon") {
                val principal = call.authentication.principal<JWTPrincipal>()
                val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@post call.respond(
                    HttpStatusCode.BadRequest
                )
                val inParams = call.receive<PostInsertPokemon>()
                call.respond(pokemonBuildController.insertPokemon(inParams, authId))
            }

            post("post_good") {
                val principal = call.authentication.principal<JWTPrincipal>()
                val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@post call.respond(
                    HttpStatusCode.BadRequest
                )
                val inParams = call.receive<PostUpdateGood>()
                call.respond(pokemonBuildController.updateGood(inParams, authId))
            }

            post("post_ev") {
                val principal = call.authentication.principal<JWTPrincipal>()
                val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@post call.respond(
                    HttpStatusCode.BadRequest
                )
                val inParams = call.receive<PostUpdateEv>()
                call.respond(pokemonBuildController.updateEv(inParams, authId))
            }

            post("post_tag") {
                val principal = call.authentication.principal<JWTPrincipal>()
                val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@post call.respond(
                    HttpStatusCode.BadRequest
                )
                val inParams = call.receive<PostUpdateTag>()
                call.respond(pokemonBuildController.updateTag(inParams, authId))
            }

            post("post_moves") {
                val principal = call.authentication.principal<JWTPrincipal>()
                val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@post call.respond(
                    HttpStatusCode.BadRequest
                )
                val inParams = call.receive<PostUpdateMoves>()
                call.respond(pokemonBuildController.updateMoves(inParams, authId))
            }

            post("post_ability") {
                val principal = call.authentication.principal<JWTPrincipal>()
                val authId = principal?.payload?.getClaim("sub")?.asString() ?:
                return@post call.respond(HttpStatusCode.BadRequest)
                val inParams = call.receive<PostUpdateAbility>()
                call.respond(pokemonBuildController.updateAbility(inParams, authId))
            }

            post("post_nature") {
                val principal = call.authentication.principal<JWTPrincipal>()
                val authId = principal?.payload?.getClaim("sub")?.asString() ?:
                return@post call.respond(HttpStatusCode.BadRequest)
                val inParams = call.receive<PostUpdateNature>()
                call.respond(pokemonBuildController.updateNature(inParams, authId))
            }

            delete("delete_pokemon") {
                val principal = call.authentication.principal<JWTPrincipal>()
                val authId = principal?.payload?.getClaim("sub")?.asString() ?:
                return@delete call.respond(HttpStatusCode.BadRequest)
                val inParams = call.receive<PostDeletePokemon>()
                call.respond(pokemonBuildController.deletePokemon(inParams, authId))
            }

            route("grown-pokemon") {
                get {
                    val principal = call.authentication.principal<JWTPrincipal>()
                    val authId = principal?.payload?.getClaim("sub")?.asString() ?:
                    return@get call.respond(HttpStatusCode.BadRequest)
                    // call.respond(pokemonBuildController.getGrownPokemonList(authId))
                }
                post {
                    val principal = call.authentication.principal<JWTPrincipal>()
                    val authId = principal?.payload?.getClaim("sub")?.asString() ?:
                    return@post call.respond(HttpStatusCode.BadRequest)
                    val inParams = call.receive<GrownPokemon>()
                    // call.respond(pokemonBuildController.insertGrownPokemon(inParams, authId))
                }
                route("{id}"){
                    get {
                        val id = call.parameters["id"]?.toIntOrNull() ?:
                        return@get call.respond(HttpStatusCode.BadRequest)
                        val principal = call.authentication.principal<JWTPrincipal>()
                        val authId = principal?.payload?.getClaim("sub")?.asString() ?:
                        return@get call.respond(HttpStatusCode.BadRequest)
                        // call.respond(pokemonBuildController.getGrownPokemon(id, authId))
                    }
                    put {
                        val id = call.parameters["id"]?.toIntOrNull() ?:
                        return@put call.respond(HttpStatusCode.BadRequest)
                        val principal = call.authentication.principal<JWTPrincipal>()
                        val authId = principal?.payload?.getClaim("sub")?.asString() ?:
                        return@put call.respond(HttpStatusCode.BadRequest)
                        val inParams = call.receive<PostUpdateGrownPokemon>()
                        call.respond(pokemonBuildController.updateGrownPokemonById(id, inParams, authId))
                    }
                    delete {
                        val id = call.parameters["id"]?.toIntOrNull() ?:
                        return@delete call.respond(HttpStatusCode.BadRequest)
                        val principal = call.authentication.principal<JWTPrincipal>()
                        val authId = principal?.payload?.getClaim("sub")?.asString() ?:
                        return@delete call.respond(HttpStatusCode.BadRequest)
                        call.respond(pokemonBuildController.deletePokemon(id, authId))
                    }
                    put("value") {
                        val id = call.parameters["id"]?.toIntOrNull() ?:
                            return@put call.respond(HttpStatusCode.BadRequest)
                        val principal = call.authentication.principal<JWTPrincipal>()
                            val authId = principal?.payload?.getClaim("sub")?.asString() ?:
                        return@put call.respond(HttpStatusCode.BadRequest)
                        val inParams = call.receive<PostUpdateGrownPokemon2>()
                        call.respond(pokemonBuildController.updateGrownPokemonByValue(id, inParams, authId))
                    }
                }

            }

        }
    }
}