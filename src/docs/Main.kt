//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

suspend fun fetchData(): String {
    delay(3000)
    return "Data from server"
}

/*--------------------------- Chapter 1: The Old Ways (Blocking Calls) --------------------------*/

/*

fun fetchData(): String {
    Thread.sleep(3000)
    return "Data from server"
}

fun main() {
    println("Start fetching ... .")
    val data = fetchData()
    println("Data received: $data")
}*/

/*-------------------- Chapter 2: The Arrival of Coroutines (Swift Messengers) ---------------------------*/

/*
fun main() = runBlocking {
    println("Start fetching...")
    launch {
        val data = fetchData()
        println("Data received: $data")
    }
    println("Waiting for data...")
}*/

/*------------------- Chapter 3: The Powerful Dispatchers (Choosing the Right Messengers) --------------------------*/

/*fun main(): Unit = runBlocking {
    launch(Dispatchers.IO) { // Best for network and database calls
        val data = fetchData()
        println("Data received: $data")
    }
    println("Updating UI...")
}*/

/*--------------- Chapter 4: Async vs Launch – The Dual Messengers --------------------------------*/

/*
 *   Key Difference:
 *   launch {} – Fire-and-forget (no result).
 *   async {} – Returns a result using await().
 */

/*fun main() = runBlocking {
    val result = async { fetchData() } // Returns a Deferred value (Promise)
    println("Waiting...")
    println("Data received: ${result.await()}") // Retrieves the result
}*/

/*------------------------- Chapter 5: Handling Errors – The Kingdom’s Shields ----------------------------------*/

val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    println("Caught an error: ${throwable.message}")
}

/*fun main() = runBlocking {
    val job = launch(exceptionHandler) {
        throw Exception("something went wrong!")
    }
    job.join()
}*/

/*------------------------- Chapter 6: The Flow of Information (Replacing Callbacks & LiveData) -------------------*/

/*fun main() = runBlocking {
    fetchNumbers().collect { value ->
        println("Received: $value")
    }
    launch {
        fetchNumbers().collect { value ->
            println("Received launch: $value")
        }
    }
    println("Received")
}

fun fetchNumbers(): Flow<Int> = flow {
    for (i in 1..5) {
        delay(1000)
        emit(i) // Send value
    }
}*/

/*------------------------------------------------------------------------------------------------------------------*/

/*fun main(){
    runBlocking {
       delay(4000)
       println("A")
    }

    runBlocking {
        println("B")
    }
}*/

/*------------------------------------------------------------------------------------------------------------------*/

/*// Sequentially executes doWorld followed by "Done"
fun main() = runBlocking {
    launch { doWorld() }
    println("Done")
}

// Concurrently executes both sections
suspend fun doWorld() = coroutineScope { // this: CoroutineScope
    launch {
        delay(2000L)
        println("World 2")
    }
    launch {
        delay(1000L)
        println("World 1")
    }
    println("Hello")
}*/

/*------------------------------------------------------------------------------------------------------------------*/

/*fun main() = runBlocking {
    doWorld()
    println("Done")
}

// Concurrently executes both sections
suspend fun doWorld() = coroutineScope { // this: CoroutineScope
    launch {
        delay(2000L)
        println("World 2")
    }
    launch {
        delay(1000L)
        println("World 1")
    }
    println("Hello")
}*/

/*------------------------------------------------------------------------------------------------------------------*/

/*fun main() = runBlocking {
    launch {
        delay(1000L)
        println("World!")
    }
    println("Hello ")
}*/

/*------------------------------------------------------------------------------------------------------------------*/

/*fun main() = runBlocking {
    val job = launch { // launch a new coroutine and keep a reference to its Job
        delay(1000L)
        println("World!")
    }
    println("Hello")
    job.join() // wait until child coroutine completes
    println("Done")
}*/

/*------------------------------------------------------------------------------------------------------------------*/

/*
* the following code launches 50,000 distinct coroutines that each waits 5 seconds and then prints a
* period ('.') while consuming very little memory*
*/

/*
fun main() = runBlocking {
    repeat(50_000) { // launch a lot of coroutines
        launch {
            delay(5000L)
            print(".")
        }
    }
}*/

/*------------------------------------------------------------------------------------------------------------------*/

/*
fun main() = runBlocking {
    repeat(5000){
        println(it)
    }
}*/

/*------------------------------------------------------------------------------------------------------------------*/

/*fun main() = runBlocking {
    repeat(50_000) { // launch a lot of coroutines
        thread {
            Thread.sleep(5000L)
            print(".")
        }
    }
}*/

/*------------------------------------------------------------------------------------------------------------------*/

/*fun main() = runBlocking {
    repeat(50_000) { // launch a lot of coroutines
        thread {
            Thread.sleep(5000L)
            print(".")
        }
    }
}*/

/*------------------------------------------------------------------------------------------------------------------*/

/*fun main() = runBlocking {
    val startTime = System.currentTimeMillis()

    withContext(NonCancellable){

    }
    val job = launch {
        var nextPrintTime = startTime
        var i = 0

        while (isActive) {
            try {
                // print a message twice a second
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            } catch (exception: Exception) {
                println("$exception")
            }
        }
    }
    delay(1000)
    job.cancelAndJoin()
    println("main: Now I can quit.")
}*/

/*------------------------------------------------------------------------------------------------------------------*/

suspend fun doSomethingUsefulOne(): Int {
    delay(1000)
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000)
    return 29
}

/*fun main() = runBlocking {
    val time = measureTimeMillis {
        val one = doSomethingUsefulOne()
        val two = doSomethingUsefulTwo()
        println("The answer is ${one + two}")
    }
    println("Completed in $time")
}*/

/*------------------------------------------------------------------------------------------------------------------*/

/*fun main() = runBlocking {
    val time = measureTimeMillis {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time")
}*/

/*------------------------------------------------------------------------------------------------------------------*/

/*
* Note that if we just call await in println without first calling start on individual coroutines, this will lead to
* sequential behavior, since await starts the coroutine execution and waits for its finish,
* which is not the intended use-case for laziness.
* */

/*fun main() = runBlocking {
    val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
        val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
        one.start()
        two.start()
        println("The answer is ${one.await() + two.await()}")
    }

    println("Completed in $time")
}*/

/*------------------------------------------------------------------------------------------------------------------*/

/*fun main() = runBlocking {
    val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
    val job = launch {
        delay(1000)
        println("I am from launch body!")
        one.start()
        print("One is ${one.await()}")
    }
    job.join()
}*/

/*------------------------------------------------------------------------------------------------------------------*/

/*
* Note that these xxxAsync functions are not suspending functions. They can be used from anywhere.
* */
/*fun main() {
    GlobalScope.async {

    }
}*/

/*------------------------------------------------------------------------------------------------------------------*/

// reference to opt-out of the structured concurrency.
// The result type of somethingUsefulOneAsync is Deferred<Int>
@OptIn(DelicateCoroutinesApi::class)
fun somethingUsefulOneAsync() = GlobalScope.async {
    doSomethingUsefulOne()
}

// The result type of somethingUsefulTwoAsync is Deferred<Int>
@OptIn(DelicateCoroutinesApi::class)
fun somethingUsefulTwoAsync() = GlobalScope.async {
    doSomethingUsefulTwo()
}

/*------------------------------------------------------------------------------------------------------------------*/
// Structured concurrency with async

suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    one.await() + two.await()
}

/*fun main() = runBlocking {
    val time = measureTimeMillis {
        println("The answer is ${concurrentSum()}")
    }
    println("Completed in $time ms")
}*/

/*------------------------------------------------------------------------------------------------------------------*/

// Cancellation is always propagated through coroutines hierarchy:
/*
fun main() = runBlocking<Unit> {
    try {
        failedConcurrentSum()
    } catch (e: ArithmeticException) {
        println("Computation failed with ArithmeticException")
    }
}
*/

suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async<Int> {
        try {
            delay(Long.MAX_VALUE) // Emulates very long computation
            42
        } finally {
            println("First child was cancelled")
        }
    }
    val two = async<Int> {
        println("Second child throws an exception")
        throw ArithmeticException()
    }
    one.await() + two.await()
}

/*------------------------------------------------------------------------------------------------------------------*/

/*@OptIn(ExperimentalCoroutinesApi::class)
fun main() = runBlocking<Unit> {

    // Section 1: Launch without a context
    // When launch { ... } is used without parameters, it inherits the context (and thus dispatcher)
    // from the CoroutineScope it is being launched from. In this case, it inherits the context of the
    // main runBlocking coroutine which runs in the main thread.
    launch {
        println("main runBlocking               : I am working in thread ${Thread.currentThread().name}")
    }

    // Section 2: Launch with Dispatchers.Unconfined
    // Dispatchers.Unconfined is a special dispatcher that also appears to run in the main thread,
    // but it is, in fact, a different mechanism that is explained later.
    // It does not confine the coroutine to any specific thread, and it resumes on the thread that was
    // used when the coroutine was suspended.
    launch(context = Dispatchers.Unconfined) {
        println("Unconfined                     : I'm working in thread ${Thread.currentThread().name}")
    }

    // Section 3: Launch with Dispatchers.Default
    // The default dispatcher is used when no other dispatcher is explicitly specified in the scope.
    // It is represented by Dispatchers.Default and uses a shared background pool of threads.
    launch(context = Dispatchers.Default) {
        println("Default                        : I'm working in thread ${Thread.currentThread().name}")
    }

    // Section 4: Launch with newSingleThreadContext
    // newSingleThreadContext creates a thread for the coroutine to run. A dedicated thread is a very
    // expensive resource. In a real application, it must be either released, when no longer needed,
    // using the close function, or stored in a top-level variable and reused throughout the application.
    launch(newSingleThreadContext("MyOwnThread")) {
        println("newSingleThreadContext         : I'm working in thread ${Thread.currentThread().name}")
    }
}*/

/*------------------------------------------------------------------------------------------------------------------*/

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

// It demonstrates several new techniques. One is using runBlocking with an explicitly specified context,
// and the other one is using the withContext function to change the context of a coroutine
// while still staying in the same coroutine

/*@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
fun main() {
    newSingleThreadContext("Ctx1").use { ctx1 ->
        newSingleThreadContext("Ctx2").use { ctx2 ->
            runBlocking(ctx1) {
                log("Started in ctx1")

                withContext(ctx2) {
                    log("Working in ctx2")
                }
                log("Back to ctx1")
            }
        }
    }
}*/

/*------------------------------------------------------------------------------------------------------------------*/

// Job in the context
// The coroutine's Job is part of its context, and can be retrieved from it using the coroutineContext[Job] expression:
/*fun main() = runBlocking {
    println("My job is ${coroutineContext[Job]}")
}*/

/*------------------------------------------------------------------------------------------------------------------*/

/*
fun main() = runBlocking {
    val mainScope = MainScope()
    // launch a coroutine to process some kind of incoming request
    val request = launch {
        // it spawns two other jobs
        launch(Job()) {
            println("job1: I run in my own Job and execute independently!")
            delay(1000)
            println("job1: I am not affected by cancellation of the request")
        }
        // and the other inherits the parent context
        launch {
            delay(100)
            println("job2: I am a child of the request coroutine")
            delay(1000)
            println("job2: I will not execute this line if my parent request is cancelled")
        }
    }
    delay(500)
    request.cancel() // cancel processing of the request
    println("main: Who has survived request cancellation?")
    delay(1000) // delay the main thread for a second to see what happens
}*/
