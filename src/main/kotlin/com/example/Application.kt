package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * The main entry point for the Ktor application.
 * This function configures and starts the embedded Netty web server.
 */
fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

/**
 * The main Ktor module.
 * This function is called by the server to configure the application's services and routing.
 * It sets up content negotiation for JSON and defines the API endpoints.
 */
fun Application.module() {
    // Configure content negotiation to use kotlinx.serialization for JSON.
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    // In a real application, this would be injected using a dependency injection framework like Koin or Dagger.
    val searchService: SearchService = FlareSolverrSearchService()

    // Defines the routing for the application.
    routing {
        /**
         * A simple health check endpoint.
         * Responds with "Hello, World!" to indicate that the server is running.
         */
        get("/") {
            call.respondText("Hello, World!")
        }

        /**
         * Groups all search-related endpoints under the `/search` path.
         */
        route("/search") {
            /**
             * Searches for people by name.
             *
             * **Path:** `/search/name/{name}`
             * **Method:** GET
             * @param name The name of the person to search for.
             */
            get("/name/{name}") {
                val name = call.parameters["name"] ?: return@get call.respondText("Missing name")
                val result = searchService.searchByName(name)
                call.respond(result)
            }

            /**
             * Searches for people by address.
             *
             * **Path:** `/search/address/{address}`
             * **Method:** GET
             * @param address The address to search for.
             */
            get("/address/{address}") {
                val address = call.parameters["address"] ?: return@get call.respondText("Missing address")
                val result = searchService.searchByAddress(address)
                call.respond(result)
            }

            /**
             * Searches for people by phone number.
             *
             * **Path:** `/search/phone/{phone}`
             * **Method:** GET
             * @param phone The phone number to search for.
             */
            get("/phone/{phone}") {
                val phone = call.parameters["phone"] ?: return@get call.respondText("Missing phone")
                val result = searchService.searchByPhone(phone)
                call.respond(result)
            }

            /**
             * Searches for people by email address.
             *
             * **Path:** `/search/email/{email}`
             * **Method:** GET
             * @param email The email address to search for.
             */
            get("/email/{email}") {
                val email = call.parameters["email"] ?: return@get call.respondText("Missing email")
                val result = searchService.searchByEmail(email)
                call.respond(result)
            }
        }

        /**
         * Retrieves a person by their ID.
         *
         * **Path:** `/person/{id}`
         * **Method:** GET
         * @param id The unique ID of the person to retrieve.
         */
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
