package tasks

import contributors.*
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

suspend fun loadContributorsNotCancellable(service: GitHubService, req: RequestData): List<User> = run {
    service.getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()
        .map { repo ->
            GlobalScope.async {
                log("starting loading for ${repo.name}")
                delay(3000L) // To be able to cancel operation before perform the requests
                service.getRepoContributors(req.org, repo.name)
                    .also { logUsers(repo, it) }
                    .bodyList()
            }
        }.awaitAll()
        .flatten()
        .aggregate()
}