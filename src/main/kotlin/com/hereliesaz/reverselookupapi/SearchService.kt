package com.hereliesaz.reverselookupapi

import kotlinx.serialization.Serializable

/**
 * Represents a person's details.
 * This data class is used for serialization to JSON.
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
 * Represents the result of a search, containing a list of matching persons.
 * This data class is used for serialization to JSON.
 */
@Serializable
data class SearchResult(
    val matches: List<Person>
)

/**
 * Defines the contract for a search service.
 * This interface allows for different implementations (e.g., mock vs. real).
 */
interface SearchService {
    /**
     * Searches for people by name.
     * @param name The name to search for.
     * @return A [SearchResult] containing a list of matching people.
     */
    suspend fun searchByName(name: String): SearchResult

    /**
     * Searches for a person by address.
     * @param address The address to search for.
     * @return A [SearchResult] containing the most likely match.
     */
    fun searchByAddress(address: String): SearchResult

    /**
     * Searches for a person by phone number.
     * @param phone The phone number to search for.
     * @return A [SearchResult] containing the most likely match.
     */
    fun searchByPhone(phone: String): SearchResult

    /**
     * Searches for a person by email address.
     * @param email The email address to search for.
     * @return A [SearchResult] containing the most likely match.
     */
    fun searchByEmail(email: String): SearchResult

    /**
     * Gets the details of a person by their ID.
     * @param id The ID of the person to retrieve.
     * @return The [Person] object, or null if not found.
     */
    fun getPersonById(id: String): Person?
}

/**
 * A mock implementation of the [SearchService] that returns hardcoded data.
 * This is a placeholder and should be replaced with a real implementation
 * that scrapes the target website.
 */
class MockSearchService : SearchService {
    private val mockData = listOf(
        Person("1", "John Smith", 30, "123 Main St, Anytown, USA", "555-1234", "john.smith@email.com"),
        Person("2", "Jane Doe", 25, "456 Oak Ave, Othertown, USA", "555-5678", "jane.doe@email.com")
    )

    override suspend fun searchByName(name: String): SearchResult {
        // In a real implementation, this would use the name to filter results.
        // For now, we return all mock data.
        return SearchResult(mockData)
    }

    override fun searchByAddress(address: String): SearchResult {
        // In a real implementation, this would use the address to find a match.
        return SearchResult(mockData.take(1)) // Return the most likely match
    }

    override fun searchByPhone(phone: String): SearchResult {
        // In a real implementation, this would use the phone to find a match.
        return SearchResult(mockData.take(1)) // Return the most likely match
    }

    override fun searchByEmail(email: String): SearchResult {
        // In a real implementation, this would use the email to find a match.
        return SearchResult(mockData.take(1)) // Return the most likely match
    }

    override fun getPersonById(id: String): Person? {
        return mockData.find { it.id == id }
    }
}
