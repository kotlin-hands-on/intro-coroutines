package tasks

import contributors.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

internal class Request9RxProgressKtTest {

    @Before
    fun setUp() {
        testScheduler.advanceTimeTo(0, TimeUnit.MILLISECONDS)
    }

    @Test
    fun loadContributorsReactiveProgress() {
        val testObserver = loadContributorsReactiveProgress(MockGithubService, testRequestData, testScheduler).test()
        testObserver.assertValueAt(0, emptyList())

        val startTime = testScheduler.now(TimeUnit.MILLISECONDS)

        concurrentProgressResults.forEachIndexed { index: Int, expected: TestResults ->
            println("index: $index, expected: $expected")
            testScheduler.advanceTimeTo(expected.timeFromStart, TimeUnit.MILLISECONDS)
            testObserver.assertValueAt(index + 1, expected.users)

            val time = testScheduler.now(TimeUnit.MILLISECONDS) - startTime
            Assert.assertEquals("Expected intermediate result after virtual ${expected.timeFromStart} ms:",
                expected.timeFromStart, time)
        }
        testObserver.assertComplete()
    }
}