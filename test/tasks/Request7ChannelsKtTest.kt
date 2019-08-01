package tasks

import contributors.MockGithubService
import contributors.concurrentProgressResults
import contributors.testRequestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class Request7ChannelsKtTest {
    @Test
    fun testChannels() = runBlockingTest {
        val startTime = currentTime

        var index = 0
        loadContributorsChannels(MockGithubService, testRequestData) { users, _ ->
            val expected = concurrentProgressResults[index++]
            val time = currentTime - startTime
            assertEquals(
                "Expected intermediate result after virtual ${expected.timeFromStart} ms:",
                expected.timeFromStart,
                time
            )
            assertEquals("Wrong intermediate result after $time:", expected.users, users)
        }
    }
}