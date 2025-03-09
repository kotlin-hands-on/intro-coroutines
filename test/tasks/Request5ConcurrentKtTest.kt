package tasks

import contributors.MockGithubService
import contributors.expectedConcurrentResults
import contributors.testRequestData
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Request5ConcurrentKtTest {
    @Test
    fun testConcurrent() = runBlocking {
        val startTime = System.currentTimeMillis()
        val result = loadContributorsConcurrent(MockGithubService, testRequestData)
        Assertions.assertEquals(expectedConcurrentResults.users, result, "Wrong result for 'loadContributorsConcurrent'")
        val totalTime = System.currentTimeMillis() - startTime
        /*
        // TODO: uncomment this assertion
        Assertions.assertEquals(expectedConcurrentResults.timeFromStart, totalTime,
            "The calls run concurrently, so the total virtual time should be 2200 ms: " +
                    "1000 ms for repos request plus max(1000, 1200, 800) = 1200 ms for concurrent contributors requests)"
        )
        */
        Assertions.assertTrue(
            totalTime in expectedConcurrentResults.timeFromStart..(expectedConcurrentResults.timeFromStart + 500),
            "The calls run concurrently, so the total virtual time should be 2200 ms: " +
                    "1000 ms for repos request plus max(1000, 1200, 800) = 1200 ms for concurrent contributors requests)"
        )
    }
}