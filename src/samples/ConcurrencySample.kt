package samples

import kotlinx.coroutines.*

fun main() = runBlocking {
    val deferred: Deferred<Int> = async(Dispatchers.Default) {
        loadData()
    }
    log("waiting...")
    log(deferred.await())
}

suspend fun loadData(): Int {
    log("loading...")
    delay(1000L)
    log("loaded!")
    return 42
}