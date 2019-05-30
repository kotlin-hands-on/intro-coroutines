package tasks

import contributors.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

@UseExperimental(ExperimentalCoroutinesApi::class)
class Request6ProgressKtTest {
    @Test
    fun testProgress() = runBlockingTest {
        val startTime = currentTime
        var index = 0
        loadContributorsProgress(MockGithubService, testRequestData) {
            users, _ ->
            val expected = progressResults[index++]
            val virtualTime = currentTime - startTime
            Assert.assertEquals("Expected intermediate results after virtual ${expected.timeFromStart} ms:",
                expected.timeFromStart, virtualTime)
            Assert.assertEquals("Wrong progress result after $virtualTime:", expected.users, users)
        }
    }
}