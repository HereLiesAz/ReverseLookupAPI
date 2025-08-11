package com.hereliesaz.reverselookupapi

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class OrchestratorService(
    private val cyberBackgroundChecksService: SearchService,
    private val smartBackgroundChecksService: SearchService
) : SearchService {
    override suspend fun searchByName(name: String): SearchResult = coroutineScope {
        val cyberResultsDeferred = async { cyberBackgroundChecksService.searchByName(name) }
        val smartResultsDeferred = async { smartBackgroundChecksService.searchByName(name) }

        val cyberResults = cyberResultsDeferred.await()
        val smartResults = smartResultsDeferred.await()

        val allMatches = (cyberResults.matches + smartResults.matches).distinct()
        SearchResult(allMatches)
    }

    override fun searchByAddress(address: String): SearchResult {
        TODO("Not yet implemented")
    }

    override fun searchByPhone(phone: String): SearchResult {
        TODO("Not yet implemented")
    }

    override fun searchByEmail(email: String): SearchResult {
        TODO("Not yet implemented")
    }

    override fun getPersonById(id: String): Person? {
        TODO("Not yet implemented")
    }
}
