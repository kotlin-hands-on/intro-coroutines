package tasks

import contributors.*
import kotlinx.coroutines.*

suspend fun loadContributorsConcurrent(service: GitHubService, req: RequestData): List<User> = coroutineScope {
    service.getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()
        .map { repo -> getRepoContributorsAsync(service, req, repo) }
        .awaitAll()
        .flatten()
        .aggregate()
}

private fun CoroutineScope.getRepoContributorsAsync(service: GitHubService, req: RequestData, repo: Repo) = async {
    log("starting loading for ${repo.name}")
    service.getRepoContributors(req.org, repo.name)
        .also { repoUsersResponse -> logUsers(repo, repoUsersResponse) }
        .bodyList()
}