package tasks

import contributors.MockGithubService
import contributors.expectedResults
import contributors.testRequestData
import org.junit.Assert
import org.junit.Test

class Request1BlockingKtTest {
    @Test
    fun testAggregation() {
        val users = loadContributorsBlocking(MockGithubService, testRequestData)
        Assert.assertEquals("List of contributors should be sorted " +
                "by the number of contributions in a descending order",
            expectedResults.users, users)
    }
}