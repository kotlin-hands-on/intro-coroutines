import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

/*suspend fun simple(): List<Int> {
    delay(100)
    return listOf(1, 2, 3)
}

// However, this computation blocks the main thread that is running the code.
// Using the List<Int> result type, means we can only return all the values at once.
fun main() = runBlocking<Unit> {
    simple().forEach { value -> println(value) }
    println("letzte Zeile!")
}*/

// --------------------------------------------------------------------------------------------------------------------

/*fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}

fun main() = runBlocking {
    launch {
        for (i in 1..3) {
            println("I am not blocked $i")
            delay(100)
        }
    }
    simple().collect{ value -> println(value) }
}*/

// --------------------------------------------------------------------------------------------------------------------

// Flows are cold
// This is a key reason the simple function (which returns a flow) is not marked with suspend modifier.
// The simple() call itself returns quickly and does not wait for anything. The flow starts afresh every time
// it is collected and that is why we see "Flow started" every time we call collect again.
/*
fun simple() = flow {
    println("Flow started")
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}

fun main() = runBlocking {
    println("Calling simple function ...")
    val flow = simple()
    println("Calling collect ...")
    flow.collect { value -> println(value) }
    println("Calling collect again...")
    flow.collect { value -> println(value) }
}
*/

// --------------------------------------------------------------------------------------------------------------------

// Flow cancellation basics

/*fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100)
        println("Emitting $i")
        emit(i)
    }
}

fun main() = runBlocking {
    withTimeoutOrNull(250){
        simple().collect { value -> println(value) }
    }
    println("Done.")
}*/

// --------------------------------------------------------------------------------------------------------------------

suspend fun performRequest(request: Int): String {
    delay(1000)
    return "response $request"
}

/*
* The basic operators have familiar names like map and filter. An important difference of these operators
* from sequences is that blocks of code inside these operators can call suspending functions.
* */
/*fun main() = runBlocking {
    (1..3).asFlow()
        .map { req -> performRequest(req) }
        .onEach { println(it) }
        .collect { res -> println(res) }
}*/

// --------------------------------------------------------------------------------------------------------------------

// Transform operator

/*
fun numbers(): Flow<Int> = flow {
    try {
        emit(1)
        emit(2)
        println("This line will not execute")
        emit(3)
    } finally {
        println("Finally in numbers")
    }
}

//Size-limiting intermediate operators like take cancel the execution of the flow when the corresponding
// limit is reached. Cancellation in coroutines is always performed by throwing an exception.
fun main() = runBlocking {
    numbers()
        .take(2) // take only the first two
        .collect { value -> println(value) }
}*/

// --------------------------------------------------------------------------------------------------------------------

/*fun main() = runBlocking{
    val sum = (1..5).asFlow()
        .map { it * it }
        .reduce { a, b -> a + b }
    println(sum)
}*/

// --------------------------------------------------------------------------------------------------------------------

// Flows are sequential
/*
fun main() = runBlocking {
    (1..5).asFlow()
        .filter {
            println("Filter $it")
            it % 2 == 0
        }
        .map {
            println("Map $it")
            "string $it"
        }.collect {
            println("Collect $it")
        }
}*/

// --------------------------------------------------------------------------------------------------------------------

/*
fun simple(): Flow<Int> = flow {
    log("Started simple flow")
    for (i in 1..3) {
        emit(i)
    }
}

fun main() = runBlocking {
    simple().collect { value -> log("Collected $value") }
}*/

// --------------------------------------------------------------------------------------------------------------------

/*fun simple(): Flow<Int> = flow {
    // The WRONG way to change context for CPU-consuming code in flow builder
    kotlinx.coroutines.withContext(Dispatchers.Default) {
        for (i in 1..3) {
            Thread.sleep(100) // pretend we are computing it in CPU-consuming way
            emit(i) // emit next value
        }
    }
}

fun main() = runBlocking<Unit> {
    simple().collect { value -> println(value) }
}*/


// --------------------------------------------------------------------------------------------------------------------

fun simple(): Flow<Int> = flow {
        for (i in 1..3) {
            Thread.sleep(100) // pretend we are computing it in CPU-consuming way
            emit(i) // emit next value
        }
}.flowOn(Dispatchers.Default)

fun main() = runBlocking<Unit> {
    simple().collect { value -> println(value) }
}