package com.sasakirione.pokebuild.plugins

import com.sasakirione.pokebuild.controller.PokemonDataController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.pokemonDataRoute() {
    val pokemonDataController: PokemonDataController by inject()

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
}