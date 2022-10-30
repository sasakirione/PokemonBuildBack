package com.sasakirione.pokebuild.plugins

import com.sasakirione.pokebuild.controller.PokemonBuildController
import com.sasakirione.pokebuild.controller.PostInsertPokemon
import com.sasakirione.pokebuild.controller.PostUpdateGrownPokemon
import com.sasakirione.pokebuild.controller.PostUpdateGrownPokemon2
import com.sasakirione.pokebuild.domain.BuildWithoutPokemonList
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
        route("pokemon-build") {
            route("builds") {
                get {
                    val principal = call.authentication.principal<JWTPrincipal>()
                    val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@get call.respond(
                        HttpStatusCode.BadRequest
                    )
                    call.respond(pokemonBuildController.getBuildList(authId))
                }

                post {
                    val principal = call.authentication.principal<JWTPrincipal>()
                    val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@post call.respond(
                        HttpStatusCode.BadRequest
                    )
                    val build = call.receive<BuildWithoutPokemonList>()
                    call.respond(pokemonBuildController.createBuild(build, authId))
                }

                route("/{id}") {
                    get {
                        val id =
                            call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                        val principal = call.authentication.principal<JWTPrincipal>()
                        val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@get call.respond(
                            HttpStatusCode.BadRequest
                        )
                        call.respond(pokemonBuildController.getBuildById(id, authId))
                    }

                    put {
                        val id =
                            call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
                        val principal = call.authentication.principal<JWTPrincipal>()
                        val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@put call.respond(
                            HttpStatusCode.BadRequest
                        )
                        val build = call.receive<BuildWithoutPokemonList>()
                        call.respond(pokemonBuildController.updateBuild(id, build, authId))
                    }

                    delete {
                        val id =
                            call.parameters["id"]?.toIntOrNull()
                                ?: return@delete call.respond(HttpStatusCode.BadRequest)
                        val principal = call.authentication.principal<JWTPrincipal>()
                        val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@delete call.respond(
                            HttpStatusCode.BadRequest
                        )
                        call.respond(pokemonBuildController.deleteBuild(id, authId))
                    }

                    route("pokemon") {
                        get {
                            val id =
                                call.parameters["id"]?.toIntOrNull()
                                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                            val principal = call.authentication.principal<JWTPrincipal>()
                            val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@get call.respond(
                                HttpStatusCode.BadRequest
                            )
                            call.respond(pokemonBuildController.getPokemonListFromBuild(id, authId))
                        }

                        post {
                            val principal = call.authentication.principal<JWTPrincipal>()
                            val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@post call.respond(
                                HttpStatusCode.BadRequest
                            )
                            val inParams = call.receive<PostInsertPokemon>()
                            call.respond(pokemonBuildController.insertPokemon(inParams, authId))
                        }

                        route("/{pokemonId}") {
                            get {
                                val id =
                                    call.parameters["id"]?.toIntOrNull()
                                        ?: return@get call.respond(HttpStatusCode.BadRequest)
                                val pokemonId =
                                    call.parameters["pokemonId"]?.toIntOrNull() ?: return@get call.respond(
                                        HttpStatusCode.BadRequest
                                    )
                                val principal = call.authentication.principal<JWTPrincipal>()
                                val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@get call.respond(
                                    HttpStatusCode.BadRequest
                                )
                                call.respond(pokemonBuildController.getPokemonByIdFromBuild(id, pokemonId, authId))
                            }

                            post {
                                val id =
                                    call.parameters["id"]?.toIntOrNull()
                                        ?: return@post call.respond(HttpStatusCode.BadRequest)
                                val pokemonId =
                                    call.parameters["pokemonId"]?.toIntOrNull() ?: return@post call.respond(
                                        HttpStatusCode.BadRequest
                                    )
                                val principal = call.authentication.principal<JWTPrincipal>()
                                val authId =
                                    principal?.payload?.getClaim("sub")?.asString() ?: return@post call.respond(
                                        HttpStatusCode.BadRequest
                                    )
                                call.respond(pokemonBuildController.addPokemon(id, pokemonId, authId))
                            }

                            delete {
                                val id =
                                    call.parameters["id"]?.toIntOrNull()
                                        ?: return@delete call.respond(HttpStatusCode.BadRequest)
                                val pokemonId =
                                    call.parameters["pokemonId"]?.toIntOrNull() ?: return@delete call.respond(
                                        HttpStatusCode.BadRequest
                                    )
                                val principal = call.authentication.principal<JWTPrincipal>()
                                val authId =
                                    principal?.payload?.getClaim("sub")?.asString() ?: return@delete call.respond(
                                        HttpStatusCode.BadRequest
                                    )
                                call.respond(pokemonBuildController.deletePokemonFromBuild(id, pokemonId, authId))
                            }
                        }
                    }
                }
            }

            route("grown-pokemons") {
                get {
                    val principal = call.authentication.principal<JWTPrincipal>()
                    val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@get call.respond(
                        HttpStatusCode.BadRequest
                    )
                    call.respond(pokemonBuildController.getGrownPokemonList(authId))
                }
                post {
                    val principal = call.authentication.principal<JWTPrincipal>()
                    val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@post call.respond(
                        HttpStatusCode.BadRequest
                    )
                    val inParams = call.receive<GrownPokemon>()
                    call.respond(pokemonBuildController.insertGrownPokemon(inParams, authId))
                }
                route("{id}") {
                    get {
                        val id =
                            call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                        val principal = call.authentication.principal<JWTPrincipal>()
                        val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@get call.respond(
                            HttpStatusCode.BadRequest
                        )
                        call.respond(pokemonBuildController.getGrownPokemon(id, authId))
                    }
                    put {
                        val id =
                            call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
                        val principal = call.authentication.principal<JWTPrincipal>()
                        val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@put call.respond(
                            HttpStatusCode.BadRequest
                        )
                        val inParams = call.receive<PostUpdateGrownPokemon>()
                        call.respond(pokemonBuildController.updateGrownPokemonById(id, inParams, authId))
                    }
                    delete {
                        val id = call.parameters["id"]?.toIntOrNull()
                            ?: return@delete call.respond(HttpStatusCode.BadRequest)
                        val principal = call.authentication.principal<JWTPrincipal>()
                        val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@delete call.respond(
                            HttpStatusCode.BadRequest
                        )
                        call.respond(pokemonBuildController.deletePokemon(id, authId))
                    }
                    put("value") {
                        val id =
                            call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
                        val principal = call.authentication.principal<JWTPrincipal>()
                        val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@put call.respond(
                            HttpStatusCode.BadRequest
                        )
                        val inParams = call.receive<PostUpdateGrownPokemon2>()
                        call.respond(pokemonBuildController.updateGrownPokemonByValue(id, inParams, authId))
                    }
                }
            }
        }
        route("public-build/{id}") {
            post("on") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@post call.respond(HttpStatusCode.BadRequest)
                val principal = call.authentication.principal<JWTPrincipal>()
                val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@post call.respond(
                    HttpStatusCode.BadRequest
                )
                call.respond(pokemonBuildController.makePublicBuild(id, authId))
            }
            post("off") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@post call.respond(HttpStatusCode.BadRequest)
                val principal = call.authentication.principal<JWTPrincipal>()
                val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@post call.respond(
                    HttpStatusCode.BadRequest
                )
                call.respond(pokemonBuildController.makePrivateBuild(id, authId))
            }
        }
    }

    route("public-build") {
        get {
            call.respond(pokemonBuildController.getPublicBuildList())
        }

        route("{id}") {
            get {
                val id =
                    call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                call.respond(pokemonBuildController.getPublicBuild(id))
            }
            get("is-public") {
                val id =
                    call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                call.respond(pokemonBuildController.isPublicBuild(id))
            }
        }
    }
}