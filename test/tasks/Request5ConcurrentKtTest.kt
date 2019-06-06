package tasks

import contributors.MockGithubService
import contributors.expectedConcurrentResults
import contributors.testRequestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

@UseExperimental(ExperimentalCoroutinesApi::class)
class Request5ConcurrentKtTest {
    @Test
    fun testConcurrent() = runBlockingTest {
        val startTime = currentTime
        val result = loadContributorsConcurrent(MockGithubService, testRequestData)
        Assert.assertEquals("Wrong result for 'loadContributorsConcurrent'", expectedConcurrentResults.users, result)
        val totalTime = currentTime - startTime
        Assert.assertEquals(
            "The calls run concurrently, so the total virtual time should be 2200 ms: " +
                    "1000 ms for repos request plus max(1000, 1200, 800) = 1200 ms for concurrent contributors requests)",
            expectedConcurrentResults.timeFromStart, totalTime
        )
    }
}