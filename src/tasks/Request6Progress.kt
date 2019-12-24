package tasks

import contributors.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

suspend fun loadContributorsProgress(
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) {
/*    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .body() ?: listOf()

    var allUsers = emptyList<User>()
    for ((i, repo) in repos.withIndex()){
        val users = service.getRepoContributors(req.org, repo.name)
            .also { logUsers(repo, it) }
            .bodyList()
        allUsers = (allUsers + users).aggregate()
        updateResults(allUsers, i == repos.lastIndex)
    }*/
    TODO()
}
