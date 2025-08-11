# Unofficial Reverse Lookup API

This project provides an unofficial API for performing reverse lookups of names, addresses, phone numbers, and emails. It is designed to scrape search results from the website `smartbackgroundchecks.com`.

## Description

The application is a Ktor-based web server written in Kotlin. It exposes a set of RESTful endpoints for searching people. Since the target website is protected by Cloudflare, the application uses [FlareSolverr](https://github.com/FlareSolverr/FlareSolverr) to bypass the protection and retrieve the HTML content.

## Features

-   Search for people by name, address, phone number, or email.
-   Retrieves search results by scraping a target website.
-   Uses FlareSolverr to bypass Cloudflare.
-   Provides a RESTful JSON API.

## Prerequisites

To run this application, you will need the following installed on your system:

-   Java 21
-   Gradle 8.8 or later
-   A running instance of [FlareSolverr](https://github.com/FlareSolverr/FlareSolverr). By default, the application expects FlareSolverr to be running at `http://localhost:8191`.

## Installation & Setup

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd <repository-directory>
    ```

2.  **Set up FlareSolverr:**
    Follow the instructions on the [FlareSolverr GitHub page](https://github.com/FlareSolverr/FlareSolverr) to set up and run a FlareSolverr instance. The easiest way is to use Docker:
    ```bash
    docker run -d --name flaresolverr -p 8191:8191 -e LOG_LEVEL=info ghcr.io/flaresolverr/flaresolverr:latest
    ```

3.  **Configure the application (optional):**
    The FlareSolverr URL and the target website URL are configured in the `FlareSolverrSearchService.kt` file. You can modify the default values in the constructor if needed.

## Running the application

To run the application, use the following Gradle command:

```bash
./gradlew run
```

The server will start and listen on port `8080`.

## API Endpoints

The API provides the following endpoints for searching:

-   `GET /search/name/{name}`: Search for people by name.
-   `GET /search/address/{address}`: Search for a person by address.
-   `GET /search/phone/{phone}`: Search for a person by phone number.
-   `GET /search/email/{email}`: Search for a person by email address.
-   `GET /person/{id}`: Get the details of a person by their ID.

All endpoints return a JSON response. The search endpoints return a `SearchResult` object containing a list of matching persons. The person endpoint returns a `Person` object.
