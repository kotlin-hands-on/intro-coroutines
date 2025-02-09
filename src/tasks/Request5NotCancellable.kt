package tasks

import contributors.*
import kotlinx.coroutines.*

suspend fun loadContributorsNotCancellable(service: GitHubService, req: RequestData): List<User> {
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req = req, it) }
        .bodyList()

    val deferred: List<Deferred<List<User>>> = repos.map { repo ->
        log("starting loading for ${repo.name}")
        //delay(3000)
        GlobalScope.async {
            service.getRepoContributors(req.org, repo.name)
                .also { logUsers(repo, it) }
                .bodyList()
        }
    }
    return deferred.awaitAll().flatten().aggregate()
}