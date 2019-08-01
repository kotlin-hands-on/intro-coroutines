package tasks

import contributors.MockGithubService
import contributors.progressResults
import contributors.testRequestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class Request6ProgressKtTest {
    @Test
    fun testProgress() = runBlockingTest {
        val startTime = currentTime

        var index = 0
        loadContributorsProgress(MockGithubService, testRequestData) { users, _ ->
            val expected = progressResults[index++]
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