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

        val userFlow = channelFlow {
            for (repo in repos){
                launch {
                    val users = service.getRepoContributors(req.org, repo.name)
                    send(users)
                }
            }
        }

        var allUsers = emptyList<User>()
        userFlow.collectIndexed { i, users ->
            allUsers = (allUsers + users).aggregate()
            updateResults(allUsers, i == repos.lastIndex)
        }
    }
}