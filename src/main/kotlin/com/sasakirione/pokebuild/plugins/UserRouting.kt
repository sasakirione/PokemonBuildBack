package com.sasakirione.pokebuild.plugins

import com.sasakirione.pokebuild.controller.Setting
import com.sasakirione.pokebuild.controller.UserController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoute() {
    val userController: UserController by inject()

    authenticate {
        route("user") {
            route("setting") {
                get {
                    val principal = call.authentication.principal<JWTPrincipal>()
                    val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@get call.respond(
                        HttpStatusCode.BadRequest
                    )
                    call.respond(userController.getSetting(authId))
                }
                post {
                    val principal = call.authentication.principal<JWTPrincipal>()
                    val authId = principal?.payload?.getClaim("sub")?.asString() ?: return@post call.respond(
                        HttpStatusCode.BadRequest
                    )
                    val inParams = call.receive<Setting>()
                    call.respond(userController.updateSetting(authId, inParams))
                }
            }
        }
    }
}