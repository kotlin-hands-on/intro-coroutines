package tasks

import contributors.MockGithubService
import contributors.progressResults
import contributors.testRequestData
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
            val time = currentTime - startTime
            Assert.assertEquals("Expected intermediate result after virtual ${expected.timeFromStart} ms:",
                expected.timeFromStart, time)
            Assert.assertEquals("Wrong intermediate result after $time:", expected.users, users)
        }
    }
}