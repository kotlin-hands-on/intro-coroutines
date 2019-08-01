package tasks

import contributors.MockGithubService
import contributors.expectedResults
import contributors.testRequestData
import org.junit.Assert.assertEquals
import org.junit.Test

class Request3CallbacksKtTest {
    @Test
    fun testDataIsLoaded() {
        loadContributorsCallbacks(MockGithubService, testRequestData) { users ->
            assertEquals(
                "Wrong result for 'loadContributorsCallbacks'",
                expectedResults.users, users
            )
        }
    }
}