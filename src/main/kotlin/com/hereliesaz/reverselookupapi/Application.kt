package com.hereliesaz.reverselookupapi

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * The main entry point of the application.
 * This function starts the embedded Netty server.
 */
fun main() {
    try {
        embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
            .start(wait = true)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * The main Ktor module.
 * This function is called by the server to configure the application.
 */
fun Application.module() {
    // Configure content negotiation to use kotlinx.serialization
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    // Create an instance of the search service.
    // In a real application, this would be injected using a dependency injection framework.
    val cyberBackgroundChecksService = CyberBackgroundChecksService()
    val smartBackgroundChecksService = SmartBackgroundChecksService()
    val searchService: SearchService = OrchestratorService(cyberBackgroundChecksService, smartBackgroundChecksService)

    // Configure the routing for the application.
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        route("/search") {
            /**
             * The search endpoints.
             * These endpoints are grouped under the `/search` path.
             */
            get("/name/{name}") {
                val name = call.parameters["name"] ?: return@get call.respondText("Missing name")
                val result = searchService.searchByName(name)
                call.respond(result)
            }
            get("/address/{address}") {
                val address = call.parameters["address"] ?: return@get call.respondText("Missing address")
                val result = searchService.searchByAddress(address)
                call.respond(result)
            }
            get("/phone/{phone}") {
                val phone = call.parameters["phone"] ?: return@get call.respondText("Missing phone")
                val result = searchService.searchByPhone(phone)
                call.respond(result)
            }
            get("/email/{email}") {
                val email = call.parameters["email"] ?: return@get call.respondText("Missing email")
                val result = searchService.searchByEmail(email)
                call.respond(result)
            }
        }
        get("/person/{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText("Missing id")
            val person = searchService.getPersonById(id)
            if (person != null) {
                call.respond(person)
            } else {
                call.respondText("Person not found")
            }
        }
    }
}
