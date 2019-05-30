package tasks

import contributors.MockGithubService
import contributors.expectedConcurrentResults
import contributors.expectedResults
import contributors.testRequestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

@UseExperimental(ExperimentalCoroutinesApi::class)
class Request4SuspendKtTest {
    @Test
    fun testSuspend() = runBlockingTest {
        val startTime = currentTime
        val result = loadContributorsSuspend(MockGithubService, testRequestData)
        Assert.assertEquals("Wrong result for 'loadContributorsSuspend'", expectedResults.users, result)
        val virtualTime = currentTime - startTime
        Assert.assertEquals(
            "The calls run consequently, so the total virtual time should be 4000 ms: " +
                    "1000 for repos request plus (1000 + 1200 + 800) = 3000 for contributors sequential requests)",
            expectedResults.timeFromStart, virtualTime
        )
    }
}