package tasks

import contributors.MockGithubService
import contributors.expectedResults
import contributors.testRequestData
import org.junit.Assert.assertEquals
import org.junit.Test

class Request1BlockingKtTest {
    @Test
    fun testAggregation() {
        val users = loadContributorsBlocking(MockGithubService, testRequestData)

        assertEquals(
            "List of contributors should be sorted " +
                    "by the number of contributions in a descending order",
            expectedResults.users,
            users
        )
    }
}