package tasks

import contributors.MockGithubService
import contributors.expectedConcurrentResults
import contributors.testRequestData
import contributors.testScheduler
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

internal class Request8RxKtTest {

    @Before
    fun setUp() {
        testScheduler.advanceTimeTo(0, TimeUnit.MILLISECONDS)
    }

    @Test
    fun loadContributorsReactive() {
        val testObserver = loadContributorsReactive(MockGithubService, testRequestData, testScheduler).test()
        testObserver.assertNoValues()

        val startTime = testScheduler.now(TimeUnit.MILLISECONDS)
        testScheduler.advanceTimeBy(expectedConcurrentResults.timeFromStart, TimeUnit.MILLISECONDS)
        testObserver.assertValue(expectedConcurrentResults.users)

        val totalTime = testScheduler.now(TimeUnit.MILLISECONDS) - startTime
        Assert.assertEquals(
            "The calls run concurrently, so the total virtual time should be 2200 ms: " +
                    "1000 ms for repos request plus max(1000, 1200, 800) = 1200 ms for concurrent contributors requests)",
            expectedConcurrentResults.timeFromStart, totalTime
        )
    }

}