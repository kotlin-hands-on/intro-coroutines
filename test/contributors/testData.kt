package contributors

val testRequestData = RequestData("username", "password", "org")

data class TestRepo(val name: String, val delay: Long, val users: List<User>)

data class TestResults(val timeFromStart: Long, val users: List<User>)

val testRepos = listOf(
    TestRepo(
        "repo-1", 1000, listOf(
            User("user-1", 10),
            User("user-2", 20)
        )
    ),
    TestRepo(
        "repo-2", 1200, listOf(
            User("user-2", 30),
            User("user-1", 40)
        )
    ),
    TestRepo(
        "repo-3", 800, listOf(
            User("user-2", 50),
            User("user-3", 60)
        )
    )
)

const val getReposDelay = 1000L

val repos = testRepos.mapIndexed { index, testRepo -> Repo(index.toLong(), testRepo.name) }

val reposMap = testRepos.associate { it.name to it }

val expectedResults = TestResults(
    4000,
    listOf(
        User("user-2", 100),
        User("user-3", 60),
        User("user-1", 50)
    )
)

val expectedConcurrentResults = TestResults(
    2200,
    expectedResults.users
)

val progressResults = listOf(
    TestResults(
        2000,
        listOf(User(login = "user-2", contributions = 20), User(login = "user-1", contributions = 10))
    ),
    TestResults(
        3200,
        listOf(User(login = "user-2", contributions = 50), User(login = "user-1", contributions = 50))
    ),
    expectedResults
)

val concurrentProgressResults = listOf(
    TestResults(
        1800,
        listOf(User(login = "user-3", contributions = 60), User(login = "user-2", contributions = 50))
    ),
    TestResults(
        2000,
        listOf(User(login = "user-2", contributions = 70), User(login = "user-3", contributions = 60),
            User(login = "user-1", contributions = 10))
    ),
    expectedConcurrentResults
)