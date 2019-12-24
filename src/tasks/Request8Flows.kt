package tasks

import contributors.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

suspend fun loadContributorsFlows(
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) {
    coroutineScope {
        val repos = service.getOrgRepos(req.org)
            /*.also { logRepos(req, it) }*/

        val userFlow = flow {
            for (repo in repos){
                launch {
                    val users = service.getRepoContributors(req.org, repo.name)
                    emit(users)
                }
            }
        }

//        var allUsers = emptyList<User>()
//        repeat(repos.size) {
//            val users = channel.receive()
//            allUsers = (allUsers + users).aggregate()
//            updateResults(allUsers, it == repos.lastIndex)
//        }
/*        userFlow
            .scanReduce { accumulator, value -> accumulator + value }
            .collectIndexed { index, value -> updateResults(value, index == repos.lastIndex) }*/

        val counter = AtomicInteger(0)
        var allUsers = emptyList<User>()
        userFlow.collectIndexed { i, users ->
            allUsers = (allUsers + users).aggregate()
            updateResults(allUsers, i == repos.lastIndex)
        }
    }
}