package com.sasakirione.pokebuild.plugins

import com.sasakirione.pokebuild.controller.PokemonDataController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.pokemonDataRoute() {
    val pokemonDataController: PokemonDataController by inject()

    route("pokemon-data") {
        route("pokemon") {
            get {
                call.respond(pokemonDataController.getPokemonList())
            }
            route("/{id}") {
                get {
                    val id = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                    call.respond(pokemonDataController.getPokemon(id))
                }
                get("moves") {
                    val id = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                    // call.respond(pokemonDataController.getPokemonMoves(id))
                }
                get("abilities") {
                    val id = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                    // call.respond(pokemonDataController.getPokemonAbilities(id))
                }
            }
        }
        route("goods") {
            get {
                call.respond(pokemonDataController.getGoods())
            }
            get("/{id}") {
                val id = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                //call.respond(pokemonDataController.getGood(id))
            }
        }
        route("moves") {
            get {
                call.respond(pokemonDataController.getMoves())
            }
            get("/{id}") {
                val id = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                //call.respond(pokemonDataController.getGood(id))
            }
        }
        route("abilities") {
            get {
                // call.respond(pokemonDataController.getAbilities())
            }
            get("/{id}") {
                val id = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                //call.respond(pokemonDataController.getGood(id))
            }
        }
        route("nature") {
            get {
                // call.respond(pokemonDataController.getNature())
            }
        }
        route("tag") {
            get {
                call.respond(pokemonDataController.getTags())
            }
            get("/{id}") {
                val id = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                //call.respond(pokemonDataController.getGood(id))
            }
        }
    }
}