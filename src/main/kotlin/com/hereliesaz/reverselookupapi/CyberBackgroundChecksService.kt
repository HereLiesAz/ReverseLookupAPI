package com.hereliesaz.reverselookupapi

import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.util.concurrent.TimeUnit
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.support.ui.ExpectedConditions
import java.time.Duration

class CyberBackgroundChecksService : SearchService {
    override suspend fun searchByName(name: String): SearchResult = withContext(Dispatchers.IO) {
        println("Starting search on CyberBackgroundChecksService for $name")
        // Set up headless Chrome browser
        val options = ChromeOptions()
        options.addArguments("--headless")
        // The following arguments are needed to run Chrome in a container
        options.addArguments("--no-sandbox")
        options.addArguments("--disable-dev-shm-usage")
        val driver = ChromeDriver(options)
        println("ChromeDriver created for CyberBackgroundChecksService")

        try {
            // Navigate to the website
            driver.get("https://www.cyberbackgroundchecks.com/")
            println("Navigated to cyberbackgroundchecks.com")

            val wait = WebDriverWait(driver, Duration.ofSeconds(10))
            val nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("input")))
            nameInput.sendKeys(name)

            // Find and click the search button
            val searchButton = driver.findElement(By.tagName("button"))
            searchButton.click()

            // Wait for the results to load, this is a simple way to wait
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)

            // After a search, the url changes to /search?name=...
            // The results are in a div with class "search-results"
            // Each result is a div with class "record"
            val resultElements = driver.findElements(By.className("record"))

            val people = resultElements.mapNotNull { element ->
                try {
                    val personName = element.findElement(By.className("name")).text
                    val age = element.findElement(By.className("age")).text.replace("Age: ", "").toIntOrNull() ?: 0
                    val address = element.findElement(By.className("address")).text
                    // The id can be extracted from the link to the person's profile
                    val id = element.findElement(By.tagName("a")).getAttribute("href").split("/").last()
                    Person(
                        id = id,
                        name = personName,
                        age = age,
                        address = address,
                        phone = "", // Not available in the summary view
                        email = ""  // Not available in the summary view
                    )
                } catch (e: Exception) {
                    // If an element is not found, skip this person
                    null
                }
            }

            SearchResult(people)
        } finally {
            // Close the browser
            driver.quit()
        }
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
