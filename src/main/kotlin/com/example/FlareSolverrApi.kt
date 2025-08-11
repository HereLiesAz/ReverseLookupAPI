package com.example

import kotlinx.serialization.Serializable

/**
 * Represents a request to create a new FlareSolverr session.
 *
 * @property cmd The command, which should be "sessions.create".
 */
@Serializable
data class FlareSolverrSessionCreateRequest(
    val cmd: String = "sessions.create"
)

/**
 * Represents the response from a session creation request.
 *
 * @property status The status of the request.
 * @property message A message from FlareSolverr.
 * @property session The ID of the created session.
 */
@Serializable
data class FlareSolverrSessionResponse(
    val status: String,
    val message: String,
    val session: String
)

/**
 * Represents a request to destroy a FlareSolverr session.
 *
 * @property cmd The command, which should be "sessions.destroy".
 * @property session The ID of the session to destroy.
 */
@Serializable
data class FlareSolverrSessionDestroyRequest(
    val cmd: String = "sessions.destroy",
    val session: String
)

/**
 * Represents the JSON request body for a general FlareSolverr `/v1` endpoint command.
 *
 * @property cmd The command to execute (e.g., "request.get").
 * @property url The URL to request.
 * @property session The session ID to use for the request (optional).
 * @property maxTimeout The maximum timeout for the request in milliseconds.
 */
@Serializable
data class FlareSolverrRequest(
    val cmd: String,
    val url: String,
    val session: String? = null,
    val maxTimeout: Int = 60000
)

/**
 * Represents the JSON response body from the FlareSolverr `/v1` endpoint.
 *
 * @property status The status of the request (e.g., "ok" or "error").
 * @property message A message from FlareSolverr, usually present in case of an error.
 * @property solution The solution object containing the response from the target website.
 */
@Serializable
data class FlareSolverrResponse(
    val status: String,
    val message: String,
    val solution: FlareSolverrSolution
)

/**
 * Represents the solution part of the FlareSolverr response.
 *
 * @property url The final URL after any redirects.
 * @property status The HTTP status code of the response.
 * @property response The HTML content of the response.
 * @property userAgent The user agent used for the request.
 */
@Serializable
data class FlareSolverrSolution(
    val url: String,
    val status: Int,
    val response: String,
    val userAgent: String
)
