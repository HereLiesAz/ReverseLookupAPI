package com.hereliesaz.reverselookupapi

import kotlinx.serialization.Serializable

/**
 * Represents a person's details.
 *
 * This data class encapsulates the information about a single person,
 * including their ID, name, age, address, phone number, and email.
 * It is marked as serializable for conversion to and from JSON.
 *
 * @property id The unique identifier for the person.
 * @property name The full name of the person.
 * @property age The age of the person.
 * @property address The physical address of the person.
 * @property phone The phone number of the person.
 * @property email The email address of the person.
 */
@Serializable
data class Person(
    val id: String,
    val name: String,
    val age: Int,
    val address: String,
    val phone: String,
    val email: String
)

/**
 * Represents the result of a search operation.
 *
 * This data class holds a list of [Person] objects that match the search criteria.
 * It is marked as serializable for conversion to and from JSON.
 *
 * @property matches A list of [Person] objects that matched the search.
 */
@Serializable
data class SearchResult(
    val matches: List<Person>
)

/**
 * Defines the contract for a search service.
 *
 * This interface provides a set of methods for searching for people using different criteria.
 * It is designed to be implemented by different search providers, such as a mock service
 * for testing or a real service that interacts with a web scraper.
 */
interface SearchService {
    /**
     * Searches for people by their full name.
     *
     * @param name The name to search for.
     * @return A [SearchResult] containing a list of people who match the name.
     */
    suspend fun searchByName(name: String): SearchResult

    /**
     * Searches for people by their physical address.
     *
     * @param address The address to search for.
     * @return A [SearchResult] containing a list of people associated with the address.
     */
    suspend fun searchByAddress(address: String): SearchResult

    /**
     * Searches for people by their phone number.
     *
     * @param phone The phone number to search for.
     * @return A [SearchResult] containing a list of people associated with the phone number.
     */
    suspend fun searchByPhone(phone: String): SearchResult

    /**
     * Searches for people by their email address.
     *
     * @param email The email address to search for.
     * @return A [SearchResult] containing a list of people associated with the email address.
     */
    suspend fun searchByEmail(email: String): SearchResult

    /**
     * Retrieves the details of a specific person by their unique ID.
     *
     * @param id The unique identifier of the person to retrieve.
     * @return The [Person] object if found, otherwise `null`.
     */
    suspend fun getPersonById(id: String): Person?
}

/**
 * A mock implementation of the [SearchService] for testing purposes.
 *
 * This class returns a hardcoded list of [Person] objects and does not perform any real
 * search operations. It is useful for testing the API endpoints without relying on the
 * web scraper or external services.
 */
class MockSearchService : SearchService {
    private val mockData = listOf(
        Person("1", "John Smith", 30, "123 Main St, Anytown, USA", "555-1234", "john.smith@email.com"),
        Person("2", "Jane Doe", 25, "456 Oak Ave, Othertown, USA", "555-5678", "jane.doe@email.com")
    )

    override suspend fun searchByName(name: String): SearchResult {
        return SearchResult(mockData.filter { it.name.contains(name, ignoreCase = true) })
    }

    override suspend fun searchByAddress(address: String): SearchResult {
        return SearchResult(mockData.filter { it.address.contains(address, ignoreCase = true) })
    }

    override suspend fun searchByPhone(phone: String): SearchResult {
        return SearchResult(mockData.filter { it.phone == phone })
    }

    override suspend fun searchByEmail(email: String): SearchResult {
        return SearchResult(mockData.filter { it.email.equals(email, ignoreCase = true) })
    }

    override suspend fun getPersonById(id: String): Person? {
        return mockData.find { it.id == id }
    }
}
