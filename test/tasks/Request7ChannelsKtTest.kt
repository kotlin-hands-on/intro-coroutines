package tasks

import contributors.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

@UseExperimental(ExperimentalCoroutinesApi::class)
class Request7ChannelsKtTest {
    @Test
    fun testChannels() = runBlockingTest {
        val startTime = currentTime
        var index = 0
        loadContributorsChannels(MockGithubService, testRequestData) {
                users, _ ->
            val expected = concurrentProgressResults[index++]
            val virtualTime = currentTime - startTime
            Assert.assertEquals("Expected intermediate results after virtual ${expected.timeFromStart} ms:",
                expected.timeFromStart, virtualTime)
            Assert.assertEquals("Wrong progress result after $virtualTime:", expected.users, users)
        }

    }
}