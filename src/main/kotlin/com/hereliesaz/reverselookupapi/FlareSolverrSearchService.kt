package com.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

@Serializable
data class FlareSolverrRequest(
    val cmd: String,
    val url: String,
    val maxTimeout: Int = 60000
)

@Serializable
data class FlareSolverrResponse(
    val status: String,
    val message: String,
    val solution: FlareSolverrSolution
)

@Serializable
data class FlareSolverrSolution(
    val url: String,
    val status: Int,
    val response: String,
    val userAgent: String
)

/**
 * An implementation of the [SearchService] that uses FlareSolverr to bypass Cloudflare
 * and scrape search results from a target website.
 *
 * @param flaresolverrUrl The URL of the FlareSolverr instance.
 * @param targetUrl The base URL of the website to scrape.
 */
class FlareSolverrSearchService(
    private val flaresolverrUrl: String = "http://localhost:8191",
    private val targetUrl: String = "https://smartbackgroundchecks.com"
) : SearchService {

    private val client = HttpClient(CIO)

    private suspend fun getDocument(url: String): Document? {
        val flareSolverrRequest = FlareSolverrRequest(
            cmd = "request.get",
            url = url
        )

        try {
            val response: FlareSolverrResponse = client.post("$flaresolverrUrl/v1") {
                contentType(ContentType.Application.Json)
                setBody(flareSolverrRequest)
            }.body()

            return if (response.status == "ok") {
                Jsoup.parse(response.solution.response)
            } else {
                println("FlareSolverr error: ${response.message}")
                null
            }
        } catch (e: Exception) {
            println("Error during search: ${e.message}")
            return null
        }
    }

    private fun parseSearchResults(document: Document): SearchResult {
        // This parsing logic is a guess and will likely need to be adjusted.
        val matches = document.select(".record").map { element ->
            val personName = element.select("h2").text()
            val age = element.select(".age").text().toIntOrNull() ?: 0
            val address = element.select(".address").text()
            // The ID will be part of the URL to the person's details page.
            val id = element.select("a").attr("href").split("/").last()

            Person(id, personName, age, address, "", "") // Phone and email are not on the results page
        }
        return SearchResult(matches)
    }

    override suspend fun searchByName(name: String): SearchResult {
        val searchUrl = "$targetUrl/search/name/${name.replace(" ", "-")}"
        val document = getDocument(searchUrl)
        return if (document != null) parseSearchResults(document) else SearchResult(emptyList())
    }

    override suspend fun searchByAddress(address: String): SearchResult {
        val searchUrl = "$targetUrl/search/address/${address.replace(" ", "-")}"
        val document = getDocument(searchUrl)
        return if (document != null) parseSearchResults(document) else SearchResult(emptyList())
    }

    override suspend fun searchByPhone(phone: String): SearchResult {
        val searchUrl = "$targetUrl/search/phone/$phone"
        val document = getDocument(searchUrl)
        return if (document != null) parseSearchResults(document) else SearchResult(emptyList())
    }

    override suspend fun searchByEmail(email: String): SearchResult {
        val searchUrl = "$targetUrl/search/email/$email"
        val document = getDocument(searchUrl)
        return if (document != null) parseSearchResults(document) else SearchResult(emptyList())
    }

    override suspend fun getPersonById(id: String): Person? {
        val personUrl = "$targetUrl/person/$id"
        val document = getDocument(personUrl)

        return if (document != null) {
            // This parsing logic is a guess for the person details page.
            val name = document.select(".name").text()
            val age = document.select(".age").text().toIntOrNull() ?: 0
            val address = document.select(".address").text()
            val phone = document.select(".phone").text()
            val email = document.select(".email").text()
            Person(id, name, age, address, phone, email)
        } else {
            null
        }
    }
}
