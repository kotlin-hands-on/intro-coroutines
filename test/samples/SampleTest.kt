package samples

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SampleTest {
    @Test
    fun testDelayInSuspend() = runTest {
        val realStartTime = System.currentTimeMillis()
        val virtualStartTime = currentTime

        foo()

        println("${System.currentTimeMillis() - realStartTime} ms")  // ~ 6 ms
        println("${currentTime - virtualStartTime} ms")              // 1000 ms
    }

    suspend fun foo() {
        delay(1000) // auto-advances without delay
        println("foo")       // executes eagerly when foo() is called
    }

    @Test
    fun testDelayInLaunch() = runTest {
        val realStartTime = System.currentTimeMillis()
        val virtualStartTime = currentTime

        bar()

        println("${System.currentTimeMillis() - realStartTime} ms")  // ~ 11 ms
        println("${currentTime - virtualStartTime} ms")              // 1000 ms
    }

    suspend fun bar() = coroutineScope {
        launch {
            delay(1000) // auto-advances without delay
            println("bar")       // executes eagerly when bar() is called
        }
    }
}